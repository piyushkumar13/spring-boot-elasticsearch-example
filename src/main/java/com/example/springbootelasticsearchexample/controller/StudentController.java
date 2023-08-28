/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.springbootelasticsearchexample.domain.Customer;
import com.example.springbootelasticsearchexample.domain.Student;
import com.example.springbootelasticsearchexample.repository.CustomerRepository;
import com.example.springbootelasticsearchexample.repository.RepoUsingTemplate;
import com.example.springbootelasticsearchexample.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piyush Kumar.
 * @since 26/08/23.
 */

@Data
@RestController
@RequestMapping(path = "/v1")
public class StudentController {

    private final StudentRepository studentRepository;
    private final RepoUsingTemplate repoUsingTemplate;

    @RequestMapping(path = "/student", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createStudent(@RequestBody Student student) throws JsonProcessingException {

//        Student student = Student.builder()
//            .id(2)
//            .name("Gopesh")
//            .course("IT")
//            .dob(new Date())
//            .department("IT")
//            .build();

        System.out.println("Object is ::: "+ new ObjectMapper().writeValueAsString(student));

        studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
