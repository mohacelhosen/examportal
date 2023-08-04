package com.mcu.examportal.repository;

import com.mcu.examportal.entity.exam.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
