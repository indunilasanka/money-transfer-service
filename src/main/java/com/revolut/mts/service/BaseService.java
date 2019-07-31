package com.revolut.mts.service;

import com.revolut.mts.configuration.BaseRouter;
import com.revolut.mts.configuration.DatabaseConnector;
import com.revolut.mts.dao.InitialSetupDao;
import com.revolut.mts.dao.impl.InitialSetupDaoImpl;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the basic business logic of the application
 *
 * @author iasa0862 18/07/19
 */
public class BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    public void initializeDataResources() {
        LOGGER.info("Initializing data sources");
        DatabaseConnector.initializeDataSource();
        InitialSetupDao initialSetupDao = new InitialSetupDaoImpl();
        initialSetupDao.createUserAccountTable();
        initialSetupDao.createTransactionTable();
    }

    public void deployVerticleServer() {
        LOGGER.info("Initializing vertx server");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BaseRouter());
    }
}
