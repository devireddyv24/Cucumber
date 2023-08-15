package com.weichertwm.qa.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)  
public @interface TestCategory{
//    Map<String , String> categoryTestCaseList=new HashMap();
	String categories();
	//int priority() default 0;
}
