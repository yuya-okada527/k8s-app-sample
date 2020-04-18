package com.example.k8s.sample.app;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BatchDefinition {
    
    /** サンプル */
    SAMPLE("sample");
    
    public String name;
    
    public static BatchDefinition getBatch(String name) {
        
        return Stream.of(values())
                     .filter(batch -> batch.name.equals(name))
                     .findFirst()
                     .orElseThrow();
    }

}
