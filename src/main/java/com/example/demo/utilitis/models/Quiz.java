package com.example.demo.utilitis.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "quizzes")
public class Quiz {

    @Id
    @Column
    @GeneratedValue
    private long id;
    @NotNull
    @Size(min = 1)
    private String title;
    @NotNull
    @Column
    @Size(min = 1)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @NotNull
    @Column
    @Size(min = 2)
    @ElementCollection
    private List<String> options;
    @ElementCollection
    private List<Integer> answer;

    public Quiz() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer.stream().sorted().collect(Collectors.toList());
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getOptions() {
        return this.options;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Integer> getAnswer() {
        return this.answer;
    }

    public boolean validateAnswer(List<Integer> answer) {
        if (this.answer == null) {
            this.answer = new ArrayList<>();
        } else {
            Collections.sort(answer);
        }
        return answer.equals(this.answer);
    }
}