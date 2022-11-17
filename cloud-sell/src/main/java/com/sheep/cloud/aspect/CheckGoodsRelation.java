package com.sheep.cloud.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/2 星期三
 * Happy Every Coding Time~
 */

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface CheckGoodsRelation {
}
