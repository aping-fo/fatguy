package com.game.sdk.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lucky on 2018/2/28.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebHandler {
    String url();
    String description();
}
