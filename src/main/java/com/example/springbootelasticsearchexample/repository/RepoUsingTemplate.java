/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.repository;

import com.example.springbootelasticsearchexample.domain.Address;
import com.example.springbootelasticsearchexample.domain.Customer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Repository;

/**
 * @author Piyush Kumar.
 * @since 26/08/23.
 */

@Data
@Repository
public class RepoUsingTemplate {

    private static final String CUSTOMER_IDX = "customer-idx";

    private final ElasticsearchRestTemplate elasticsearchOperations;

//    private final ElasticsearchTemplate elasticsearchOperations;

    // region add request
    public void addCustomer(int id, String name){

        /* Using IndexQuery constructor */
        Customer customer = customer(id, name);

        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(String.valueOf(id));
        indexQuery.setObject(customer);

        String indexId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(CUSTOMER_IDX));

        System.out.println("The indexed document id using index query constructor :::: " + indexId);

        /* Using IndexQuery Builder */

        int newId = id + 1;

        Customer customer2 = customer(id, name+newId);

        IndexQuery indexQuery2 = new IndexQueryBuilder()
            .withId(String.valueOf(newId))
            .withObject(customer2)
            .build();

        String indexId2 = elasticsearchOperations.index(indexQuery2, IndexCoordinates.of(CUSTOMER_IDX));

        System.out.println("The indexed document id using index query builder :::: " + indexId2);

    }
    // endregion add request

    // region update request
    public void updateCustomer(int id){

        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(id))
            .withScript("ctx._source.gender=params.valueOfGender")
            .withParams(Map.of("valueOfGender", "male"))
            .build();

        elasticsearchOperations.update(updateQuery, IndexCoordinates.of(CUSTOMER_IDX));
    }
    // endregion update request

    // region search query

    public List<Customer> search() {

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address.state", "KA");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchQueryBuilder)
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithSort() {

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address.state", "KA");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithSortAndAndOperator() {

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address.city", "NEW DELHI")
            .operator(Operator.AND);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithFuziness() {

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address.city", "NEW DALHI") // Deliberately made as DALHI.
            .operator(Operator.AND)
            .fuzziness(Fuzziness.fromEdits(1));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithMatchPhrase() {

        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("address.city", "N Delhi"); // it should return empty

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchPhraseQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithMatchPhrasePrefix() {

        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery("address.city", "New");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchPhrasePrefixQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithQueryPrefix() {

        PrefixQueryBuilder prefixQuery = QueryBuilders.prefixQuery("address.state", "del"); // Note used "d" in lowercase https://stackoverflow.com/a/61187460

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(prefixQuery)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithWildCardQuery() {

        WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("address.state", "del*"); // Note used "d" in lowercase https://stackoverflow.com/a/61187460

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(wildcardQueryBuilder)
            .withSort(Sort.by(Sort.Direction.ASC, "id"))
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<Customer> searchWithBoolQuery() {

        MatchQueryBuilder query1 = QueryBuilders.matchQuery("address.state", "KA");
        MatchQueryBuilder query2 = QueryBuilders.matchQuery("address.line1", "ABC");
//        TermQueryBuilder query3 = QueryBuilders.termQuery("name", "satish");
//        TermQueryBuilder query4 = QueryBuilders.termQuery("name", "piyush");

        MatchQueryBuilder query3 = QueryBuilders.matchQuery("name", "Satish");
        MatchQueryBuilder query4 = QueryBuilders.matchQuery("name", "Piyush");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
            .must(query1)
            .filter(query2)
            .should(query3)
            .should(query4)
            .minimumShouldMatch(1);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            .build();

        SearchHits<Customer> searchResult = elasticsearchOperations.search(nativeSearchQuery, Customer.class, IndexCoordinates.of(CUSTOMER_IDX));

        return searchResult.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }
    // endregion search query

    // region update with Query
    public void updateCustomerByQuery(){

        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("address.city", "New Delhi");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(matchPhraseQueryBuilder)
            .build();

        UpdateQuery updateQuery = UpdateQuery.builder(nativeSearchQuery)
            .withScriptType(ScriptType.INLINE)
            .withScript("ctx._source['gender'] = params['newValue']")
            .withLang("painless")
            .withParams(Collections.singletonMap("newValue", "male"))
            .withAbortOnVersionConflict(true)
            .build();

        /* Following query removes the field from document. */
//        UpdateQuery updateQuery = UpdateQuery.builder(nativeSearchQuery)
//            .withScriptType(org.springframework.data.elasticsearch.core.ScriptType.INLINE)
//            .withScript("ctx._source['address'].remove('gender')")
//            .withLang("painless")
//            .withAbortOnVersionConflict(true)
//            .build();

        elasticsearchOperations.updateByQuery(updateQuery, IndexCoordinates.of(CUSTOMER_IDX));
    }

    // endregion update with Query

    // region aggregations

    /*
     * One easy way to debug aggregation request or as a matter of fact any elasticsearch request in java is to see what json request it is generating,
     * that you can findout while putting debug points at the requests which is passed to elasticsearchOperations.
     *
     * And also you can find out the curl request generated by enabling logging.level.tracer=DEBUG and then you can check what is the payload passed in it.
     * Payload should be same as json expected in the elasticsearch rest api.
     */

    public void performSumAggregations(){

        SumAggregationBuilder sumAgg = AggregationBuilders.sum("sum_agg").field("amountPaid");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withAggregations(sumAgg)
            .build();


        SearchHits<Object> search = elasticsearchOperations.search(nativeSearchQuery, Object.class, IndexCoordinates.of(CUSTOMER_IDX));

        Aggregations aggregations = ((ElasticsearchAggregations) search.getAggregations()).aggregations();


        System.out.println("The aggregation map ::: " + aggregations.getAsMap());
        System.out.println("The aggregation ::: " + aggregations.get("sum_agg"));
        System.out.println("The aggregation name ::: " + aggregations.get("sum_agg").getName());
        System.out.println("The aggregation type ::: " + aggregations.get("sum_agg").getType());
        System.out.println("The aggregation metadata ::: " + aggregations.get("sum_agg").getMetadata());
        System.out.println("The aggregation metadata ::: " + ((Sum)aggregations.get("sum_agg")).getValue());
        System.out.println("The aggregation metadata ::: " + ((Sum)aggregations.get("sum_agg")).getValueAsString());


        System.out.println(search);
    }

    public void performBucketAggregations(){

        TermsAggregationBuilder termsAgg = AggregationBuilders.terms("gender_terms").field("gender.keyword");

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withAggregations(termsAgg)
            .build();


        SearchHits<Object> search = elasticsearchOperations.search(nativeSearchQuery, Object.class, IndexCoordinates.of(CUSTOMER_IDX));

        Aggregations aggregations = ((ElasticsearchAggregations) search.getAggregations()).aggregations();


        System.out.println("The aggregation map ::: " + aggregations.getAsMap());
        System.out.println("The aggregation ::: " + aggregations.get("gender_terms"));
        System.out.println("The aggregation name ::: " + aggregations.get("gender_terms").getName());
        System.out.println("The aggregation type ::: " + aggregations.get("gender_terms").getType());
        System.out.println("The aggregation metadata ::: " + aggregations.get("gender_terms").getMetadata());
        System.out.println("The aggregation metadata ::: " + ((MultiBucketsAggregation)aggregations.get("gender_terms")).getBuckets());
//        System.out.println("The aggregation buckets ::: " + ((ParsedStringTerms)aggregations.get("gender_terms")).getBuckets()); // we can also do this

        int i = 1;
        for (MultiBucketsAggregation.Bucket bucket : ((MultiBucketsAggregation)aggregations.get("gender_terms")).getBuckets()){
            System.out.println("bucket " + i + " key ::: " + bucket.getKey());
            System.out.println("bucket " + i + " docCount ::: " + bucket.getDocCount());
            i++;
        }


        System.out.println(search);
    }

    public void performBucketAggregationsWithNestedAggregation(){

        TermsAggregationBuilder termsAgg = AggregationBuilders.terms("gender_terms")
            .field("gender.keyword")
            .subAggregation(AggregationBuilders.sum("sum_amount_paid").field("amountPaid"));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withAggregations(termsAgg)
            .build();


        SearchHits<Object> search = elasticsearchOperations.search(nativeSearchQuery, Object.class, IndexCoordinates.of(CUSTOMER_IDX));

        Aggregations aggregations = ((ElasticsearchAggregations) search.getAggregations()).aggregations();


        System.out.println("The aggregation map ::: " + aggregations.getAsMap());
        System.out.println("The aggregation ::: " + aggregations.get("gender_terms"));
        System.out.println("The aggregation name ::: " + aggregations.get("gender_terms").getName());
        System.out.println("The aggregation type ::: " + aggregations.get("gender_terms").getType());
        System.out.println("The aggregation metadata ::: " + aggregations.get("gender_terms").getMetadata());
        System.out.println("The aggregation metadata ::: " + ((MultiBucketsAggregation)aggregations.get("gender_terms")).getBuckets());
//        System.out.println("The aggregation buckets ::: " + ((ParsedStringTerms)aggregations.get("gender_terms")).getBuckets()); // we can also do this

        int i = 1;
        for (MultiBucketsAggregation.Bucket bucket : ((MultiBucketsAggregation)aggregations.get("gender_terms")).getBuckets()){
            System.out.println("bucket " + i + " key ::: " + bucket.getKey());
            System.out.println("bucket " + i + " docCount ::: " + bucket.getDocCount());
            System.out.println("bucket " + i + " subAgg ::: " + bucket.getAggregations()); // represents nested aggregations
            System.out.println("bucket " + i + " subAgg ::: " + ((Sum)bucket.getAggregations().get("sum_amount_paid")).getValue());
            i++;
        }


        System.out.println(search);
    }

    // endregion

    private Customer customer(int id, String name){

        return Customer.builder()
            .id(id)
            .name(name)
            .dob("26/08/2003")
            .address(Address.builder().line1("pqrs").city("Bangalore").state("KA").country("IN").build())
            .gender("female")
            .billingNumber(987654321)
            .amountDue(12.1)
            .amountPaid(10.1)
            .productSku(List.of("Hand bags", "Luggage bags"))
            .build();
    }

}
