package me.ckhd.opengame.drds.entity;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface MyAnnotation {
	
	//类型,1为普通类型,0为主键
	int type() default 1;

	//序号
	int index() default 100;
	
	
	String name();

}
