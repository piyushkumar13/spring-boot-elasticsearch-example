/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.repository;

import com.example.springbootelasticsearchexample.domain.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.yaml.snakeyaml.events.Event;

/**
 * @author Piyush Kumar.
 * @since 28/08/23.
 */
public interface StudentRepository extends ElasticsearchRepository<Student, Event.ID> {

}
