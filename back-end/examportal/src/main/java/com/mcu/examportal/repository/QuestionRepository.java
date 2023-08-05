package com.mcu.examportal.repository;

import com.mcu.examportal.entity.exam.Question;
import com.mcu.examportal.entity.exam.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Set<Question> findByQuiz(Quiz quiz);
}
