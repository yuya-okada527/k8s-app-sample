package com.example.k8s.sample.app.batch;

import java.io.ByteArrayInputStream;

import org.springframework.stereotype.Service;

import com.example.k8s.sample.BatchApplication.Batch;
import com.example.k8s.sample.infra.S3Client;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleBatch extends Batch {
    
    private final S3Client s3Client;
    
    @Override
    public void execute() throws Exception {
        
        String text = "sample text";
        this.s3Client.putObject(new ByteArrayInputStream(text.getBytes("utf-8")), "sample.txt", "sample/");
        
    }

}
