/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.repository;

import com.example.springbootelasticsearchexample.domain.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Piyush Kumar.
 * @since 26/08/23.
 */
public interface CustomerRepository extends ElasticsearchRepository<Customer, Integer> {

    Customer findByName(String name);

    Customer findByNameAndBillingNumber(String name, Long billingNumber);

    Customer findByNameAndGender(String name, String gender);

    Customer findByNameAndGenderAndDob(String name, String gender, String dob);

    Customer findByNameAndAddressCity(String name, String city);
}
