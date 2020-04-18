package com.example.k8s.sample.infra;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Component
public class S3Client {
    
    @Value("${aws.s3.region}")
    private String region;
    
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public void putObject(InputStream inStream, String fileName, String prefix) {
        
        // AmazonS3クライアントを取得
        AmazonS3 client = this.getClient();
        
        try {
            client.putObject(this.bucketName, prefix + fileName, inStream, new ObjectMetadata());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private AmazonS3 getClient() {
        return AmazonS3ClientBuilder.standard()
                                    .withRegion(this.region)
                                    .build();
    }
}
