package com.example.k8s.sample.app.batch;

import org.springframework.stereotype.Service;

import com.example.k8s.sample.BatchApplication.Batch;

@Service
public class SampleBatch extends Batch {
    
    @Override
    public void execute() throws Exception {
        
        System.out.println("sample");
    }

}
