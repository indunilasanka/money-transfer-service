package com.revolut.mts.controller;

import com.revolut.mts.configuration.BaseRouter;
import com.revolut.mts.model.Transaction;
import com.revolut.mts.model.UserAccount;
import com.revolut.mts.service.BaseService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

@RunWith(VertxUnitRunner.class)
public class TransactionControllerTest {
    private static Vertx vertx;
    private static int port;
    private static HttpClient client;

    //TEST CONFIGURATION METHODS
    @BeforeClass
    public static void setup(TestContext testContext) {
        try {
            ServerSocket socket;
            socket = new ServerSocket(0);
            port = socket.getLocalPort();
            socket.close();

            DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

            vertx = Vertx.vertx();
            vertx.deployVerticle(BaseRouter.class.getName(), options,
                    testContext.asyncAssertSuccess());
            client = vertx.createHttpClient();

            BaseService baseService = new BaseService();
            baseService.initializeDataResources();

            //create 3 default userAccounts for testing purposes. first 2 userAccounts[accountNumber - 10000000,10000001] will be used in transaction operations.
            //3rd userAccount [accountNumber - 10000003] will be used in UserAccount related operations
            createDefaultUserAccount1(testContext);
            createDefaultUserAccount2(testContext);
            createDefaultUserAccount3(testContext);
        } catch (IOException e) {
            Assert.fail("Failure in setting up the test environment");
        }
    }

    //TEST CONFIGURATION METHODS
    private static void createDefaultUserAccount1(TestContext testContext) {
        Async async = testContext.async();

        UserAccount defaultUserAccount1 = new UserAccount();
        defaultUserAccount1.setAccountHolder("Indunil");
        defaultUserAccount1.setBalance((double) 100);
        String json = Json.encodePrettily(defaultUserAccount1);

        client.post(port, "localhost", "/user")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    //TEST CONFIGURATION METHODS
    private static void createDefaultUserAccount2(TestContext testContext) {
        Async async = testContext.async();

        UserAccount defaultUserAccount2 = new UserAccount();
        defaultUserAccount2.setAccountHolder("Asanka");
        defaultUserAccount2.setBalance((double) 200);
        String json = Json.encodePrettily(defaultUserAccount2);

        client.post(port, "localhost", "/user")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    //TEST CONFIGURATION METHODS
    private static void createDefaultUserAccount3(TestContext testContext) {
        Async async = testContext.async();

        UserAccount defaultUserAccount3 = new UserAccount();
        defaultUserAccount3.setAccountHolder("IndunilAsanka");
        defaultUserAccount3.setBalance((double) 300);
        String json = Json.encodePrettily(defaultUserAccount3);

        client.post(port, "localhost", "/user")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @AfterClass
    public static void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }


    //TRANSACTION CONTROLLER RELATED TESTS
    @Test
    public void createTransaction_shouldReturnErrorStatus_WhenTransactionAmountIsBelowZero(TestContext testContext) {
        Async async = testContext.async();

        Transaction defaultTransaction = new Transaction();
        defaultTransaction.setSender(10000000);
        defaultTransaction.setReceiver(10000001);
        defaultTransaction.setAmount(-10);
        String json = Json.encodePrettily(defaultTransaction);
        client.post(port, "localhost", "/transaction")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Transaction amount should be greater than 0"));
                        testContext.assertTrue(body.toString().contains("2002"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void createTransaction_shouldReturnErrorStatus_WhenSenderAccountDoesNotHaveEnoughCredit(TestContext testContext) {
        Async async = testContext.async();

        Transaction defaultTransaction = new Transaction();
        defaultTransaction.setSender(10000000);
        defaultTransaction.setReceiver(10000001);
        defaultTransaction.setAmount(1000);
        String json = Json.encodePrettily(defaultTransaction);
        client.post(port, "localhost", "/transaction")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Transaction failed due to insufficient account balance"));
                        testContext.assertTrue(body.toString().contains("2002"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void createTransaction_shouldReturnSuccessStatusAndUserAccountsNeedToBeUpdated_WhenNoExceptionOccurs(TestContext testContext) {
        Async async = testContext.async();

        //Transfer 20.0 from 10000000 to 10000001
        Transaction defaultTransaction = new Transaction();
        defaultTransaction.setSender(10000000);
        defaultTransaction.setReceiver(10000001);
        defaultTransaction.setAmount(20);
        String json = Json.encodePrettily(defaultTransaction);
        client.post(port, "localhost", "/transaction")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Transaction successfull"));
                        testContext.assertTrue(body.toString().contains("1004"));
                        async.complete();
                    });
                })
                .write(json)
                .end();


        //Now assert the account balance of the sender and receiver account. Before the transaction sender owned 100.0 and receiver owned 200.0
        client.getNow(port, "localhost", "/user/10000000", response -> {
            testContext.assertEquals(response.statusCode(), 200);
            testContext.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                final UserAccount userAccount = Json.decodeValue(body.toString(), UserAccount.class);
                testContext.assertEquals(userAccount.getBalance(), 80.0);
                async.complete();
            });
        });

        client.getNow(port, "localhost", "/user/10000001", response -> {
            testContext.assertEquals(response.statusCode(), 200);
            testContext.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                final UserAccount userAccount = Json.decodeValue(body.toString(), UserAccount.class);
                testContext.assertEquals(userAccount.getBalance(), 220.0);
                async.complete();
            });
        });
    }


    //USER ACCOUNT CONTROLLER RELATED TESTS
    @Test
    public void getUserAccountForGivenAccountNumber_ShouldReturnCorrectAccount_WhenAccountAvailable(TestContext testContext) {
        Async async = testContext.async();

        client.getNow(port, "localhost", "/user/10000002", response -> {
            testContext.assertEquals(response.statusCode(), 200);
            testContext.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                final UserAccount userAccount = Json.decodeValue(body.toString(), UserAccount.class);
                testContext.assertEquals(userAccount.getAccountHolder(), "IndunilAsanka");
                testContext.assertEquals(userAccount.getBalance(), 300.0);
                async.complete();
            });
        });
    }

    @Test
    public void createNewUserAccount_ShouldReturnSuccessStatus_WhenNoExceptionOccurs(TestContext testContext) {
        Async async = testContext.async();

        UserAccount userAccount = new UserAccount();
        userAccount.setAccountHolder("NewUser");
        userAccount.setBalance((double) 400);
        String json = Json.encodePrettily(userAccount);

        client.post(port, "localhost", "/user")
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
        userAccount.setAccountHolder("NewUserWithIncorrectBalance");
        userAccount.setBalance((double) -100);
        String json = Json.encodePrettily(userAccount);

        client.post(port, "localhost", "/user")
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
    public void getUserAccountForGivenAccountNumber_ShouldReturnErrorStatus_WhenAccountNotAvailable(TestContext testContext) {
        Async async = testContext.async();

        //accountNumber 99999999 is not available in the system
        client.getNow(port, "localhost", "/user/99999999", response -> {
            testContext.assertEquals(response.statusCode(), 404);
            testContext.assertEquals(response.headers().get("content-type"), "application/json");
            response.bodyHandler(body -> {
                testContext.assertTrue(body.toString().contains("No account details"));
                testContext.assertTrue(body.toString().contains("2000"));
                async.complete();
            });
        });
    }

    @Test
    public void updateUserAccountDetails_ShouldReturnSuccessStatus_WhenNoExceptionOccurs(TestContext testContext) {
        Async async = testContext.async();

        UserAccount updatedUserAccount = new UserAccount();
        updatedUserAccount.setAccountHolder("IndunilAsanka");
        updatedUserAccount.setBalance((double) 300);
        String json = Json.encodePrettily(updatedUserAccount);

        client.put(port, "localhost", "/user/10000002")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", Integer.toString(json.length()))
                .handler(response -> {
                    testContext.assertEquals(response.statusCode(), 200);
                    testContext.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Successfully updated existing account"));
                        testContext.assertTrue(body.toString().contains("1001"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void deleteUserAccountDetails_ShouldReturnSuccessStatus_WhenNoExceptionOccurs(TestContext testContext) {
        Async async = testContext.async();

        client.delete(port, "localhost", "/user/10000002")
                .handler(response -> {
                    testContext.assertEquals(response.statusCode(), 200);
                    testContext.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        testContext.assertTrue(body.toString().contains("Successfully deleted existing account"));
                        testContext.assertTrue(body.toString().contains("1002"));
                        async.complete();
                    });
                })
                .end();
    }

}
