package com.example.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SpringBeanTool
 * @Author yupanpan
 * @Date 2019/11/19 17:31
 */

@Component
@WebListener
public class SpringBeanTool implements ApplicationContextAware, ServletContextListener {
    /**
     * 上下文对象实例
     */
    private ApplicationContext applicationContext;

    private ServletContext servletContext;

    private static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        env=applicationContext.getBean(Environment.class);
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取servletContext
     *
     * @return
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getBean(String name, Class<T> clazz) {
        Assert.hasText(name, "name为空");
        return getApplicationContext().getBean(name, clazz);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getValueApplicationPropertiesByKey(String key){
        return env.getProperty(key);
    }
}
