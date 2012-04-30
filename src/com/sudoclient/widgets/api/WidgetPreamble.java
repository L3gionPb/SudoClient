package com.sudoclient.widgets.api;

import java.lang.annotation.*;

/**
 * User: deprecated
 * Date: 4/22/12
 * Time: 5:34 AM
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WidgetPreamble {
    String name();

    String description() default "";

    String author();

    double version() default 1.0;
}
