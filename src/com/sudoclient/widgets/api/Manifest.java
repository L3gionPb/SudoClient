package com.sudoclient.widgets.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:34 AM
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {
    String name();

    String description() default "";

    String[] authors();

    double version() default 1.0;
}
