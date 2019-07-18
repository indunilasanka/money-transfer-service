package com.revolut.mts.model;

/**
 * Holds the status of an api request
 *
 * @author iasa0862 18/07/19
 */
public class Status {

    private String status;
    private int statusCode;

    public Status(String status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
