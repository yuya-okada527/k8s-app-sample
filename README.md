# k8s-app-sample

Kubernetes 環境で、動作するアプリケーションのサンプル

## デプロイ手順

### 1. インフラの構築

```bash
# ネットワークを構築
$ aws cloudformation create-stack \
--stack-name k8s-sample-app-network \
--region ap-northeast-1 \
--template-body file://./template/network.yaml

# 構築したVPCを確認
$ aws cloudformation describe-stacks \
--output json \
--stack-name "k8s-sample-app-network" | \
jq '.Stacks[].Outputs'

# S3を構築
$ aws cloudformation create-stack \
--stack-name k8s-sample-app-s3 \
--region ap-northeast-1 \
--template-body file://./template/s3.yaml \
--parameters ParameterKey=VpcEndPoint,ParameterValue={VPCエンドポイント} \
 ParameterKey=AccessIpAddress,ParameterValue={IP アドレス} \
 ParameterKey=BucketSuffix,ParameterValue={バケット接尾辞}
```

### 2. Kubernetes 環境の構築

```bash
# ワーカーノードのサブネットを確認
$ aws cloudformation describe-stacks \
--output json \
--stack-name "k8s-sample-app-network" | \
jq '.Stacks[].Outputs'

# EKSクラスタの構築
$ eksctl create cluster \
--vpc-public-subnets subnet-01bfa88196b817108,subnet-082f2d1dfdff4e32f \
--name k8s-sample-app \
--region ap-northeast-1 \
--version 1.14 \
--nodegroup-name k8s-sample-app-nodegroup \
--node-type t2.micro

# 名前空間を定義
$ kubectl apply -f manifest/namespace.yaml

# コンテキストを表示
$ kubectl config get-contexts

# コンテキストの変更
$ kubectl config set-context k8s-sample-app \
--cluster {CLUSTER列の値} \
--user {AUTHINFO列の値} \
--namespace k8s-sample-app
```

### 3. アプリケーションのコンテナ化

```bash
# アプリのビルド
$ cd batch-app
$ mvn clean package

# バッチアプリコンテナイメージの作成
$ docker build -t k8s-sample/batch-app:1.0.0 \
--build-arg JAR_FILE=target/{JAR_FILE名} \
.

# コンテナイメージのタグ付け
$ docker tag k8s-sample/batch-app:1.0.0 \
{ECRホスト}/k8s-sample/batch-app:1.0.0

# ECRにログイン
$ aws ecr get-login --no-include-email | sh

# ECRにプッシュ
$ docker push {ECRホスト}/k8s-sample/batch-app:1.0.0
```

### 4. アプリケーションのデプロイ

##### 4.1 バッチアプリのデプロイ手順

```bash
# S3バケットの確認
$ aws cloudformation describe-stacks \
--output json \
--stack-name k8s-sample-app-s3 | \
jq '.Stacks[].Outputs'

# ConfigMapの追加
$ BUCKET_NAME={バケット名} \
envsubst < manifest/config_map.yaml | \
kubectl apply -f -

# CronJobの追加
$ ECR_HOST={ECRホスト} \
envsubst < manifest/cron_job.yaml | \
kubectl apply -f -
```

##### 4.2 API のデプロイ手順

```bash
# コンテナのビルド
$ docker build -t k8s-sample/api api

# コンテナのタグ付け
$ docker tag k8s-sample/api 935326844264.dkr.ecr.ap-northeast-1.amazonaws.com/k8s-sample/api

# ECRへプッシュ
$ aws ecr get-login --no-include-email | sh
$ docker push 935326844264.dkr.ecr.ap-northeast-1.amazonaws.com/k8s-sample/api

# Deploymentの適用
$ kubectl apply -f manifest/deployment.yaml

# Serviceの適用
$ kubectl apply -f manifest/service.yaml

```

## リソースの削除手順

### 1. アプリケーションの削除

```bash
# CronJobの削除
$ kubectl delete cronjob batch-app
```

### 2. EKS クラスターの削除

```bash
$ eksctl delete cluster \
--name k8s-sample-app \
--region ap-northeast-1 \
--wait
```

### 3. AWS リソースの削除

```bash
# S3バケットの確認
$ aws cloudformation describe-stacks \
--output json
--stack-name k8s-sample-app-s3

# S3バケットを空にする
$ aws s3 rm s3://{バケット名} --recursive

# AWSリソースの削除

# Kubernetesの削除
$ eksctl delete cluster --name k8s-sample-app
```
