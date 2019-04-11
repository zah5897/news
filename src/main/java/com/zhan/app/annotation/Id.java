package com.zhan.app.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    Strategy strategy() default Strategy.IDENTITY;
}
