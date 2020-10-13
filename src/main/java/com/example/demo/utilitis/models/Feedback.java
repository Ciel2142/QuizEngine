package com.example.demo.utilitis.models;

public class Feedback {

    private final boolean success;
    private final String feedback;

    public Feedback(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getFeedback() {
        return this.feedback;
    }
}

