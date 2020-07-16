package com.example.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * 限流唯一标识
     * @return
     */
    String key() default "rate.limit:";

    /**
     * 限流时间 默认1秒
     * @return
     */
    int time() default 1;

    /**
     * 限流次数
     * @return
     */
    int count() default 100;

    /**
     *是否限制IP,默认 否
     * @return
     */
    boolean restrictionsIp() default false;
}
