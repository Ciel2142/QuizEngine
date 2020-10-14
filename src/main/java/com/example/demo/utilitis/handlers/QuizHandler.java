package com.example.demo.utilitis.handlers;

import com.example.demo.utilitis.models.*;
import com.example.demo.utilitis.repositories.CompletionRep;
import com.example.demo.utilitis.repositories.QuizRepository;
import com.example.demo.utilitis.users.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class QuizHandler {

    @Autowired
    private QuizRepository repository;
    @Autowired
    private CompletionRep completionRep;

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz newQuiz,
                        @AuthenticationPrincipal MyUserPrincipal user) {
        return repository.save(user.getUser().addQuiz(newQuiz));
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable long id,
                                        @AuthenticationPrincipal MyUserPrincipal user) {
        Quiz quiz = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (quiz.getUser() != user.getUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseEntity<?> postAnswer(@RequestBody AnswerToQuiz answer,
                                        @PathVariable long id,
                                        @AuthenticationPrincipal MyUserPrincipal userPrincipal) {

        Quiz quiz = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        if (quiz.validateAnswer(answer.getAnswer())) {
            completionRep.save(userPrincipal.getUser().
                    addCompleted(new Completed(quiz.getId(), LocalDateTime.now())));
            return new ResponseEntity<>(new Feedback(true, "Congratulations, you're right!"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Feedback(false, "Wrong answer! Please, try again."),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/api/quizzes/{id}")
    public ResponseEntity<?> getQuiz(@PathVariable long id) {
        Quiz quiz = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getQuizList(@RequestParam int page) {
        return repository.findAll(PageRequest.of(page, 10));
    }

    @GetMapping("/api/quizzes/completed")
    public Page<Completed> getCompleted(@RequestParam(required = false, defaultValue = "0") int page, @AuthenticationPrincipal MyUserPrincipal user) {
        return completionRep.findAllByUser_id(user.getUser().getId(), PageRequest.of(page, 10));
    }

    @PutMapping("api/quizzes/{id}")
    public Quiz updateQuiz(@PathVariable long id, @AuthenticationPrincipal MyUserPrincipal user, @RequestBody Quiz quiz) {
        if (id != user.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Quiz quizToUpdate = repository.getOne(id);
        quizToUpdate.setAnswer(quiz.getAnswer());
        quizToUpdate.setOptions(quiz.getOptions());
        quizToUpdate.setText(quiz.getText());
        quizToUpdate.setTitle(quiz.getTitle());
        return repository.save(quizToUpdate);
    }
}
