package com.mcu.examportal;

import com.mcu.examportal.controller.UserRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamportalApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ExamportalApplication.class);
        logger.info("ExamportalApplication::🚀");
        SpringApplication.run(ExamportalApplication.class, args);
    }

}
