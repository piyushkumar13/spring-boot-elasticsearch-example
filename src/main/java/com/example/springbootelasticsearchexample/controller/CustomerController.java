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
import com.example.springbootelasticsearchexample.repository.CustomerRepoUsingTemplate;
import com.example.springbootelasticsearchexample.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerRepoUsingTemplate customerRepoUsingTemplate;

    @RequestMapping(path = "/customer", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) throws JsonProcessingException {

//        customer = Customer.builder()
//            .id(1)
//            .name("Piyush")
//            .amountPaid(12.5)
//            .amountDue(50.0)
//            .productSku(List.of("Books", "Pens"))
//            .address(Address.builder().line1("ABC").city("Bengaluru").state("KA").country("IN").build())
//            .build();

        System.out.println("Object is ::: "+ new ObjectMapper().writeValueAsString(customer));

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @RequestMapping(path = "/customers/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable int id){

        Optional<Customer> customerOpt = customerRepository.findById(id);

        return ResponseEntity.ok(customerOpt.get());
    }

    @RequestMapping(path = "/customers",method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCustomers(){

        Iterable<Customer> customers = customerRepository.findAll();

        return ResponseEntity.ok(customers);
    }

    @RequestMapping(path = "/customers/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id){

        customerRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @RequestMapping(path = "/customers/find",method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public void findCustomers(){

        System.out.println("============================================================================================================");
        Customer findByNameAndBillingNumberCustomer = customerRepository.findByNameAndBillingNumber("Piyush", 123456789L);
        System.out.println("findByNameAndBillingNumberCustomer ::: " + findByNameAndBillingNumberCustomer);
        System.out.println("============================================================================================================");


        System.out.println("============================================================================================================");
        Customer findByNameAndGenderCustomer = customerRepository.findByNameAndGender("Piyush", "male");
        System.out.println("findByNameAndGenderCustomer ::: " + findByNameAndGenderCustomer);
        System.out.println("============================================================================================================");


        System.out.println("============================================================================================================");
        Customer findByNameAndGenderAndDobCustomer = customerRepository.findByNameAndGenderAndDob("Piyush", "male", "13/02/1990");
        System.out.println("findByNameAndGenderAndDobCustomer ::: " + findByNameAndGenderAndDobCustomer);
        System.out.println("============================================================================================================");


        System.out.println("============================================================================================================");
        Customer findByNameAndAddressCityCustomer = customerRepository.findByNameAndAddressCity("Piyush", "bengaluru");
        System.out.println("findByNameAndAddressCityCustomer ::: " + findByNameAndGenderAndDobCustomer);
        System.out.println("============================================================================================================");

    }



    /* ======================================= Using ES Templates ================================= */

    @RequestMapping(path = "/add-customer/{id}/{name}", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public void addCustomers(@PathVariable int id, @PathVariable String name){

        customerRepoUsingTemplate.addCustomer(id, name);
    }

    @RequestMapping(path = "/update-customer/{id}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public void addCustomers(@PathVariable int id){

        customerRepoUsingTemplate.updateCustomer(id);
    }

    @RequestMapping(path = "/search-customers", method = RequestMethod.GET)
    public List<Customer> searchCustomers(){

//        return customerRepoUsingTemplate.search();
//        return customerRepoUsingTemplate.searchWithSort();
//        return customerRepoUsingTemplate.searchWithSortAndAndOperator();
//        return customerRepoUsingTemplate.searchWithFuziness();
//        return customerRepoUsingTemplate.searchWithMatchPhrase();
//        return customerRepoUsingTemplate.searchWithMatchPhrasePrefix();
//        return customerRepoUsingTemplate.searchWithQueryPrefix();
//        return customerRepoUsingTemplate.searchWithWildCardQuery();
        return customerRepoUsingTemplate.searchWithBoolQuery();
    }

    @RequestMapping(path = "/update-customer-by-query", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public void updateCustomersByQuery(){

        customerRepoUsingTemplate.updateCustomerByQuery();
    }
}
