package com.example.config.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SparkConfig
 * @Author yupanpan
 * @Date 2020/5/12 11:47
 */
@Configuration
public class SparkConfig {

    @Bean
    public SparkSession sparkSession(){
        return SparkSession
                .builder()
                .enableHiveSupport()
                .sparkContext(sparkContext().sc())
                .master("local")
                .appName("program")
                .config("spark.some.config.option","some-value")
                .getOrCreate();
    }

    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf()
                .setAppName("program")
                .setMaster("local");
        return sparkConf;
    }


    @Bean
    public JavaSparkContext sparkContext(){
        return new JavaSparkContext(sparkConf())  ;
    }

    @Bean
    public SQLContext sqlContext(){
        return new SQLContext(sparkSession());
    }
}
