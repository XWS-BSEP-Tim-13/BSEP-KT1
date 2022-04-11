package com.example.backend.exception;

public class ResourceConflictException extends RuntimeException {
    private static final long serialVersionUID = 1791564636123821405L;

    private String email;

    public ResourceConflictException(String email, String message) {
        super(message);
        this.setResourceId(email);
    }

    public String getEmail() {
        return email;
    }

    public void setResourceId(String email) {
        this.email = email;
    }
}