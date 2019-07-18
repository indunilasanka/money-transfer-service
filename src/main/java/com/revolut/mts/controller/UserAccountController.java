package com.revolut.mts.controller;

import com.revolut.mts.model.UserAccount;
import com.revolut.mts.service.UserAccountService;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * Interface of user account related api operations
 *
 * @author iasa0862 18/07/19
 */
public class UserAccountController extends BaseController {

    private UserAccountService userAccountService;

    public UserAccountController() {
        userAccountService = new UserAccountService();
    }

    public void getAllUserAccounts(RoutingContext context) {
        try {
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(userAccountService.getAllUserAccounts()));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }

    public void getUserAccount(RoutingContext context) {
        try {
            int accountNumber = Integer.parseInt(context.request().getParam("accountNumber"));
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(userAccountService.getUserAccountDetails(accountNumber)));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }

    public void createNewAccount(RoutingContext context) {
        try {
            UserAccount newUserAccount = Json.decodeValue(context.getBodyAsString(), UserAccount.class);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(userAccountService.createNewUserAccount(newUserAccount)));
        } catch (Exception e) {

            baseExceptionHandler(context, e);
        }
    }

    public void updateUserAccount(RoutingContext context) {
        try {
            int accountNumber = Integer.parseInt(context.request().getParam("accountNumber"));
            UserAccount updatedUserAccount = Json.decodeValue(context.getBodyAsString(), UserAccount.class);
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(userAccountService.updateExistingUserAccount(updatedUserAccount, accountNumber)));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }

    public void deleteAccount(RoutingContext context) {
        try {
            int accountNumber = Integer.parseInt(context.request().getParam("accountNumber"));
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(userAccountService.deleteExistingUserAccount(accountNumber)));
        } catch (Exception e) {
            baseExceptionHandler(context, e);
        }
    }

}
