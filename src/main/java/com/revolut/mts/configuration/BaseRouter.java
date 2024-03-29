package com.revolut.mts.configuration;

import com.revolut.mts.controller.TransactionController;
import com.revolut.mts.controller.UserAccountController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the routing functionalists of vertx
 *
 * @author iasa0862 18/07/19
 */
public class BaseRouter extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRouter.class);
    private UserAccountController userAccountController;
    private TransactionController transactionController;

    public BaseRouter() {
        userAccountController = new UserAccountController();
        transactionController = new TransactionController();
    }

    @Override
    public void start(Future<Void> future) {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/user").handler(userAccountController::getAllUserAccounts);
        router.get("/user/:accountNumber").handler(userAccountController::getUserAccount);
        router.post("/user").handler(userAccountController::createNewAccount);
        router.put("/user/:accountNumber").handler(userAccountController::updateUserAccount);
        router.delete("/user/:accountNumber").handler(userAccountController::deleteAccount);
        router.post("/transaction").handler(transactionController::invokeTransaction);
        router.get("/transaction/:transactionId").handler(transactionController::getTransactionDetails);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 8000), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });

        LOGGER.info("Money transaction service is up and running in the 8000 port");
    }
}
