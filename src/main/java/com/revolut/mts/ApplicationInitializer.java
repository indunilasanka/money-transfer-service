package com.revolut.mts;

import com.revolut.mts.service.BaseService;

public class ApplicationInitializer {

    public static void main(String[] args) {
        BaseService baseService = new BaseService();
        baseService.initializeDataResources();
        baseService.deployVerticleServer();
    }
}
