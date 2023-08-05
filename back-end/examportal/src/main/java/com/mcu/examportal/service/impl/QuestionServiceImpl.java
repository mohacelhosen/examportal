package com.mcu.examportal.service.impl;

import com.mcu.examportal.entity.exam.Question;
import com.mcu.examportal.entity.exam.Quiz;
import com.mcu.examportal.exception.ResourceNotFoundException;
import com.mcu.examportal.repository.QuestionRepository;
import com.mcu.examportal.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository repository;
    @Override
    public Question addQuestion(Question question) {
        log.info("QuestionServiceImpl::question, "+question.toString());
        Question saveQuestion = repository.save(question);
        log.info("QuestionServiceImpl::question, saveQuestion::"+saveQuestion.toString());
        return saveQuestion;
    }

    @Override
    public Question updateQuestion(Question question) {
        return repository.save(question);
    }

    @Override
    public Question getQuestion(Integer questionId) {
        Optional<Question> question = repository.findById(questionId);
        if (question.isPresent()){
            return question.get();
        }
        throw new ResourceNotFoundException("This Question Not Found!, ID::"+questionId);
    }

    @Override
    public Set<Question> questionList() {
        List<Question> questionList = repository.findAll();
        return new HashSet<>(questionList);
    }

    @Override
    public Set<Question> getQuestionOfQuiz(Quiz quiz) {
        return  repository.findByQuiz(quiz);
    }

    @Override
    public Boolean deleteQuestion(Integer questionId) {
        Optional<Question> question = repository.findById(questionId);
        if (question.isPresent()){
            repository.delete(question.get());
            return true;
        }
        throw new ResourceNotFoundException("This Question Not Found!, ID::"+questionId);
    }
}
