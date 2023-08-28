/*
 *  Copyright (c) 2023 DMG
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO DMG
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */

package com.example.springbootelasticsearchexample.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 *
 Few observations when using above annotations :

 * Once we have assigned the field annotation with type in entity calls which has ElasticsearchRepository,
 * and application has booted up the mappings and settings will get created i.e mapping json and settings json in ES.
 * Once this mapping is created we cannot change the type by changing the type in the @Field annotation.
 * Even if we change, it will not fail the query, just the thing it will not have any effect. The mapping json will also not get changed.

 * But, when we add new fields with type their mapping will result into the mapping json in ES. But, this will only reflect
 * only after we have input any document after the addition of the new field in POJO.

 * If we add the settings using @Setting annotation also after the initial document indexed i.e mapping and default settings created,
 * we cannot change the settings or add new settings.
 * If we want to include new settings or update any of the field, we need to do reindex.
 * This can be done by changing the indexName value in @Document annotation and boot up the spring boot application .
 * This is the spring boot way. We can also reindex by using reindex REST apis.
 *
 * @author Piyush Kumar.
 * @since 28/08/23.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "student4v2")
@TypeAlias("student4v2")
@Setting(shards = 2, replicas = 2, settingPath = "sample_analyzer_v2.json") //shards and replicas are not working even when creating with new index.
public class Student4 {

    @Id
    private int id;

    @Field(name = "studentName", type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date dob;

    @Field(type = FieldType.Keyword)
    private String department;

    @MultiField(mainField = @Field(type = FieldType.Text),
    otherFields = {
        @InnerField(suffix = "keyword", type = FieldType.Keyword) // usually, suffix name used normally is keyword.
    })
    private String course;

    @Field(type = FieldType.Text)
    private String college;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date collegeStartDate;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date examDate;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date examEndDate;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date collegeEndDate;

    @Field(type = FieldType.Text, analyzer = "my_custom_analyzer_v2")
    private String mainSubject;
}
