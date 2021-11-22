package com.codeculator.foodlook.local;

import java.util.List;

public interface FetchCallback <T>{
    void preProcess();
    void postProcess(T data);
}
