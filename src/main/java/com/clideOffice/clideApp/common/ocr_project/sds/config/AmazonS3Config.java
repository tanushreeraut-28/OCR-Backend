//package com.clideOffice.clideApp.common.ocr_project.sds.config;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AmazonS3Config {
//
//    private static final String REGION = "ap-south-1"; // change if your bucket is in another region
//
//    @Bean
//    public AmazonS3 amazonS3() {
//
//        return AmazonS3ClientBuilder
//                .standard()
//                .withRegion(REGION)
//                .build();  // Uses Default AWS Credential Provider Chain
//    }
//}