package com.revolut.mts.controller;

import com.revolut.mts.model.Transaction;
import com.revolut.mts.service.TransactionService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * Interface of transaction related api operations
 *
 * @author iasa0862 18/07/19
 */
public class TransactionController extends BaseController {

    private TransactionService transactionService;

    public TransactionController() {
        transactionService = new TransactionService();
    }

    public void getTransactionDetails(RoutingContext context) {
        try {
            int transactionId = Integer.parseInt(context.request().getParam("transactionId"));
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(transactionService.getTransactionDetails(transactionId)));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }

    public void invokeTransaction(RoutingContext context) {
        try {
            Transaction newTransactionDetails = Json.decodeValue(context.getBodyAsString(), Transaction.class);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(transactionService.invokeTransaction(newTransactionDetails)));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }
}
