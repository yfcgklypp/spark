package com.example.controller;

import com.clearspring.analytics.util.Lists;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SparkTest
 * @Author yupanpan
 * @Date 2020/5/25 16:58
 */
@RestController
@RequestMapping("/spark")
public class SparkTest implements Serializable {

    @Autowired
    private transient JavaSparkContext sparkContext;

    @GetMapping("/test")
    public void test() throws IOException {


        List<String> list= Lists.newArrayList();
        list.stream().filter(record -> record.startsWith("s")).map(record ->{
                    return "<"+record+">";
                })
                .collect(Collectors.joining(","));

        //创建RDD：从文件中读取待统计文本
        JavaRDD<String> lines = sparkContext.textFile("D:\\yop_sdk_config_default.json");

        //转换RDD：将文本内容按空格分割成单词
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) {
                return Arrays.asList(s.split("\\s")).iterator();
            }
        });

        //转换RDD：将单词<String>转换成<String,Integer>,每个单词初始化次数为1
        JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        //行动RDD：统计每个单词的次数<String,Integer>
        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        });

        //获取整个RDD的数据
        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
        sparkContext.stop();
        sparkContext.close();
    }

    @GetMapping("/vmgc")
    public void vmgc(){
        byte[] data = new byte[1*1024*1024];
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        SoftReference<byte[]> softReference = new SoftReference<>(data,referenceQueue);
        data = null;
        System.out.println("before:"+softReference.get());

        try {
            for (int i = 0; i < 10; i++) {
                byte[] temp = new byte[3*1024*1024];
                System.out.println("processing:"+softReference.get());
            }
        } catch (Throwable t) {
            System.out.println("after:"+softReference.get());
            t.printStackTrace();
        }
        while(referenceQueue.poll()!=null){
            System.out.println("self:"+softReference);
            softReference.clear();
            softReference = null;
            System.out.println("last:"+softReference);
        }
    }
}
