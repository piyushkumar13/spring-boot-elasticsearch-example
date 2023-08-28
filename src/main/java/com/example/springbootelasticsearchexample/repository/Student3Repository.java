/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.repository;

import com.example.springbootelasticsearchexample.domain.Student2;
import com.example.springbootelasticsearchexample.domain.Student3;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Piyush Kumar.
 * @since 28/08/23.
 */
public interface Student3Repository extends ElasticsearchRepository<Student3, Integer> {
}
