package com.revolut.mts.controller;

import com.revolut.mts.exception.AccountNotFoundException;
import com.revolut.mts.exception.TransactionFailureException;
import com.revolut.mts.exception.TransactionNotFoundException;
import com.revolut.mts.model.Status;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static com.revolut.mts.common.Constants.StatusCodes.ACCOUNT_NOT_FOUND_CODE;
import static com.revolut.mts.common.Constants.StatusCodes.TRANSACTION_FAILURE_CODE;
import static com.revolut.mts.common.Constants.StatusCodes.TRANSACTION_NOT_FOUND_CODE;
import static com.revolut.mts.common.Constants.StatusCodes.UNEXPECTED_ERROR_CODE;
import static com.revolut.mts.common.Constants.StatusMessages.UNEXPECTED_ERROR_OCCURRED;

class BaseController {

    void baseExceptionHandler(RoutingContext context, Exception e) {
        Status errorStatus;

        if (e.getClass() == AccountNotFoundException.class) {
            errorStatus = new Status(e.getMessage(), ACCOUNT_NOT_FOUND_CODE);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(404)
                    .end(Json.encodePrettily(errorStatus));
        } else if (e.getClass() == TransactionNotFoundException.class) {
            errorStatus = new Status(e.getMessage(), TRANSACTION_NOT_FOUND_CODE);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(404)
                    .end(Json.encodePrettily(errorStatus));
        } else if (e.getClass() == TransactionFailureException.class) {
            errorStatus = new Status(e.getMessage(), TRANSACTION_FAILURE_CODE);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(400)
                    .end(Json.encodePrettily(errorStatus));
        } else {
            errorStatus = new Status(UNEXPECTED_ERROR_OCCURRED, UNEXPECTED_ERROR_CODE);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(500)
                    .end(Json.encodePrettily(errorStatus));
        }
    }
}
