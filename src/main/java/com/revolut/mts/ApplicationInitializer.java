package com.revolut.mts;

import com.revolut.mts.service.BaseService;

/**
 * Application initializer
 *
 * @author iasa0862 18/07/19
 */
public class ApplicationInitializer {

    public static void main(String[] args) {
        BaseService baseService = new BaseService();
        baseService.initializeDataResources();
        baseService.deployVerticleServer();
    }
}
