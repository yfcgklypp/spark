package com.example.exception;

/**
 * @ClassName RateLimiterException
 * @Author yupanpan
 * @Date 2020/5/7 16:28
 */
public class RateLimiterException extends Exception{

    private static final long serialVersionUID = 9209272912266977293L;

    public RateLimiterException(){ }
    public RateLimiterException(String message){
        super(message);
    }
}
