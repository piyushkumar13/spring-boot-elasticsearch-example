///*
// *  Copyright (c) 2023 DMG
// *  All Rights Reserved Worldwide.
// *
// *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
// *  AND CONSTITUTES A VALUABLE TRADE SECRET.
// */
//
//package com.example.springbootelasticsearchexample.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
//import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
///**
// * Configuring explicitly is not really required since {@link ElasticsearchRestClientAutoConfiguration} and {@link ElasticsearchDataAutoConfiguration} comes with all the default configuration.
// * By default, spring boot autoconfigurations creates the RestHighLevelClient and elasticsearchRestTemplate, though deprecated.
// * ElasticsearchRestClientAutoConfiguration is the configuration which comes with org.springframework.boot.autoconfigure package and autoconfigures
// * the elasticsearch and its rest clients and elastic search templates which points by default to http://localhost:9200 as specified in ElasticsearchProperties.
// * So, we don’t have to do much. If we want to point to some elasticsearch server whose uri we know then just provide spring.elasticsearch=<elasticsearch server uri> in application.yaml file.
// *
// * We can extend following classes which helps in configuring RestHighLevelClient, RestClient, ElasticSearchRestTemplate and ElasticsearchClient
// * * AbstractElasticsearchConfiguration which already defines bean ElasticsearchRestTemplate, we just need to define RestHighLevelClient.
// * * ElasticsearchConfiguration which already defines RestClient, ElasticsearchClient and ElasticsearchTemplate, we just need to define ClientConfiguration.
// *
// *
// * The High Level Rest Client is deprecated in favor of the Elasticsearch Java API Client.
// *
// * @author Piyush Kumar.
// * @since 26/08/23.
// */
//
//@Configuration
////@EnableElasticsearchRepositories(basePackages = "com.example.springbootelasticsearchexample.repository") // not really required
////@ComponentScan(basePackages = "com.example.springbootelasticsearchexample") // not really required
//public class ESConfigs {
//
//    /**
//     * You can uncomment any of the following classes. But, you can uncomment only once configuration class at time, otherwise, beans will conflict.
//     *
//     */
//
//
//    /**
//     * This class overrides AbstractElasticsearchConfiguration, which in creates ElasticsearchRestTemplate bean.
//     */
//    @Configuration
//    public static class ESConfigUsingRestHighLevelClient extends AbstractElasticsearchConfiguration {
//
//        @Bean
//        @Override
//        public RestHighLevelClient elasticsearchClient() {
//
//            ClientConfiguration clientConfiguration =
//                ClientConfiguration
//                    .builder()
//                    .connectedTo("localhost:9200")
//                    .build();
//
//            return RestClients.create(clientConfiguration).rest();
//        }
//    }
//
//    /**
//     * This class configures elasticseach connection by using RestClient.
//     */
//    @Configuration
//    public static class ESConfigUsingRestClient {
//
//
//        /**
//         * Below have shown two ways to configure elasticsearch which uses RestClient.
//         * Use one of the configuration at a time.
//         * */
//
//
//        /**
//         * This class overrides ElasticsearchConfiguration, which in creates RestClient, ElasticsearchClient and ElasticsearchTemplate beans.
//         */
//        @Configuration
//        public static class ESConfigUsingRestClientByExtendingESConfig extends ElasticsearchConfiguration {
//            @Bean
//            @Override
//            public ClientConfiguration clientConfiguration() {
//
//                return ClientConfiguration
//                    .builder()
//                    .connectedTo("localhost:9200")
//                    .build();
//
//            }
//        }
//
//
//        /**
//         * Another way to configure Elasticsearch where we don't need to extend ElasticsearchConfiguration class.
//         */
//        @Configuration
//        public static class AnotherWayESConfigUsingRestHighLevelClient {
//            @Bean
//            public RestClient getRestClient() {
//                RestClient restClient = RestClient.builder(
//                    new HttpHost("localhost", 9200)).build();
//                return restClient;
//            }
//
//            @Bean
//            public ElasticsearchTransport getElasticsearchTransport() {
//                return new RestClientTransport(
//                    getRestClient(), new JacksonJsonpMapper());
//            }
//
//
//            @Bean
//            public ElasticsearchClient getElasticsearchClient() {
//                ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
//                return client;
//            }
//        }
//    }
//}
