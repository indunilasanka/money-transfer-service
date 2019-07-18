package com.revolut.mts.controller;

import com.revolut.mts.configuration.BaseRouter;
import com.revolut.mts.model.Status;
import com.revolut.mts.model.UserAccount;
import com.revolut.mts.service.BaseService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

@RunWith(VertxUnitRunner.class)
public class UserAccountControllerTest {

    private static Vertx vertx;
    private static int port;

    @BeforeClass
    public static void setup(TestContext testContext) {
        try {
            BaseService baseService = new BaseService();
            baseService.initializeDataResources();

            ServerSocket socket = null;
            socket = new ServerSocket(0);
            port = socket.getLocalPort();
            socket.close();

            DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

            vertx = Vertx.vertx();
            vertx.deployVerticle(BaseRouter.class.getName(), options,
                    testContext.asyncAssertSuccess());
        } catch (IOException e) {
            Assert.fail("Failure in setting up the test environment");
        }
    }

    @Test
    public void createNewUserAccount_ShouldReturnSuccessStatus_WhenNoExceptionOccurs(TestContext testContext) {
        Async async = testContext.async();

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountHolder("Asanka");
        userAccount.setBalance((double) 100);
        String json = Json.encodePrettily(userAccount);

        vertx.createHttpClient().post(port, "localhost","/user")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    testContext.assertEquals(response.statusCode(), 200);
                    testContext.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Successfully created new account"));
                        testContext.assertTrue(body.toString().contains("1000"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void createNewUserAccount_ShouldReturnErrorStatus_WhenNewAccountBalanceIsBelowZero(TestContext testContext) {
        Async async = testContext.async();

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountHolder("Asanka");
        userAccount.setBalance((double) -100);
        String json = Json.encodePrettily(userAccount);

        vertx.createHttpClient().post(port, "localhost","/user")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    testContext.assertEquals(response.statusCode(), 400);
                    testContext.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Balance amount should be greater than 0"));
                        testContext.assertTrue(body.toString().contains("2002"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void getUser_AccountForGivenAccountNumber_ShouldReturnErrorStatus_WhenAccountNotAvailable(TestContext testContext) {
        Async async = testContext.async();

        vertx.createHttpClient().getNow(port, "localhost", "/user/99999999", response -> {
            testContext.assertEquals(response.statusCode(), 404);
            testContext.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                testContext.assertTrue(body.toString().contains("No account details"));
                testContext.assertTrue(body.toString().contains("2000"));
                async.complete();
            });
        });
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }
}
