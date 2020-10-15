package com.example.demo.utilitis.handlers;

import com.example.demo.utilitis.handlers.services.QuizService;
import com.example.demo.utilitis.models.AnswerToQuiz;
import com.example.demo.utilitis.models.Completed;
import com.example.demo.utilitis.models.Feedback;
import com.example.demo.utilitis.models.Quiz;
import com.example.demo.utilitis.users.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class QuizHandler {

    private final QuizService quizService;
    private Feedback feedback;

    @Autowired
    public QuizHandler(QuizService quizService) {
        this.quizService = quizService;
    }

    @Autowired
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    @Transactional
    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz newQuiz,
                        @AuthenticationPrincipal MyUserPrincipal user) {
        return quizService.save(user.getUser(), newQuiz);
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable long id,
                                        @AuthenticationPrincipal MyUserPrincipal user) {
        quizService.deleteById(id, user.getUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    @Transactional
    public Feedback postAnswer(@RequestBody AnswerToQuiz answer,
                               @PathVariable long id,
                               @AuthenticationPrincipal MyUserPrincipal userPrincipal) {

        Quiz quiz = quizService.findById(id);
        if (quiz.validateAnswer(answer.getAnswer())) {
            feedback.setFeedback("Congratulations, you're right!");
            feedback.setSuccess(true);
            quizService.saveCompleted(userPrincipal.getUser().
                    addCompleted(new Completed(quiz.getId(), LocalDateTime.now())));
        }
        return feedback;
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable long id) {
        return quizService.findById(id);
    }

    @GetMapping("/api/quizzes")
    public Page<Quiz> getQuizList(@RequestParam(required = false, defaultValue = "0") int page) {
        return quizService.findAll(PageRequest.of(page, 10));
    }

    @GetMapping("/api/quizzes/completed")
    public Page<Completed> getCompleted(@RequestParam(required = false, defaultValue = "0") int page, @AuthenticationPrincipal MyUserPrincipal user) {
        return quizService.findAllByUser_id(user.getUser().getId(), PageRequest.of(page, 10));
    }

    @PutMapping("api/quizzes/{id}")
    public Quiz updateQuiz(@PathVariable long id, @AuthenticationPrincipal MyUserPrincipal user, @RequestBody Quiz quiz) {
        return quizService.updateQuiz(id, quiz, user.getUser());
    }
}
