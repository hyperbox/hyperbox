package org.altherian.hboxd.hypervisor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Hypervisor {
   String id();
   
   String typeId();
   
   String vendor();
   
   String product();
   
   String[] schemes();
}
