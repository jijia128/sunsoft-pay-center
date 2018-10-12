package com.central.annotation.datasource;

import java.lang.annotation.*;

/**
 * @Description 数据源选择注解
 * @Author Derek
 * @Date 2018/10/12 14:14
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name();
}