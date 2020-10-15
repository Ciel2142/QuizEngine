package com.example.demo.utilitis.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Feedback {

    private boolean success;
    private String feedback;

    public Feedback() {
        this.feedback = "Wrong answer! Please, try again.";
        this.success = false;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getFeedback() {
        return this.feedback;
    }
}

