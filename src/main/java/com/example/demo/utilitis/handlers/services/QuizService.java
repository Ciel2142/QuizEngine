package com.example.demo.utilitis.handlers.services;

import com.example.demo.utilitis.models.Completed;
import com.example.demo.utilitis.models.Quiz;
import com.example.demo.utilitis.models.User;
import com.example.demo.utilitis.repositories.CompletionRep;
import com.example.demo.utilitis.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private CompletionRep completionRep;

    public QuizService() {
    }

    public Page<Quiz> findAll(PageRequest of) {
        return quizRepository.findAll(of);
    }

    public Quiz findById(long id) {
        return this.quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Quiz updateQuiz(long id, Quiz newQuiz, User user) {
        Quiz oldQuiz = getOne(id, user);
        oldQuiz.setText(newQuiz.getText());
        oldQuiz.setTitle(newQuiz.getTitle());
        oldQuiz.setOptions(newQuiz.getOptions());
        oldQuiz.setAnswer(newQuiz.getAnswer());
        return quizRepository.save(oldQuiz);
    }

    public Quiz save(User user, Quiz addQuiz) {
        return this.quizRepository.save(user.addQuiz(addQuiz));
    }

    private Quiz getOne(long id, User user) {
        Quiz quiz = quizRepository.getOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (quiz.getUser().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return quiz;
    }

    public void deleteById(long id, User user) {
        Quiz quiz = this.findById(id);
        if (quiz.getUser() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.deleteById(id);

    }

    public Page<Completed> findAllByUser_id(long id, PageRequest of) {
        return completionRep.findAllByUser_id(id, of);
    }

    public void saveCompleted(Completed addCompleted) {
        completionRep.save(addCompleted);
    }
}
