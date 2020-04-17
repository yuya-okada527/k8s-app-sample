package com.example.k8s.sample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.k8s.sample.app.batch.SampleBatch;

@SpringBootApplication
public class BatchApplication {
    
    /**
     * バッチ実行基底クラス
     *
     */
    public abstract static class Batch implements CommandLineRunner {
        
        public final void run(final String... args) throws Exception {
            
            // 初期化処理
            this.init(args);
        }
        
        void init(final String... args) throws Exception {
            
            // 特に初期化処理なし
        }
        
        /**
         * サブクラスで実装
         *
         * @throws Exception
         */
        public abstract void execute() throws Exception;
    }

    public static void main(String[] args) throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args)) {
            
            // Batchを起動する
            context.getBean(SampleBatch.class).execute();
        }
    }

}
