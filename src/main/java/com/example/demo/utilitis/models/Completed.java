package com.example.demo.utilitis.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Completed {


    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long pk;

    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long user_id;

    @Column
    private LocalDateTime completedAt;

    public Completed(long quizId, LocalDateTime dateTime) {
        this.id = quizId;
        this.completedAt = dateTime;
    }

    public Completed() {
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public long getPk() {
        return pk;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
