package com.example.demo.response;

import java.time.LocalDateTime;

public class Apiresponse<T> {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private T data;

    public Apiresponse() {
        this.timestamp = LocalDateTime.now();
    }

    public Apiresponse(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
