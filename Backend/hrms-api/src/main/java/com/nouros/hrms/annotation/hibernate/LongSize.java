package com.nouros.hrms.annotation.hibernate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


/**

Package com.nouros.hrms.annotation.hibernate
Provides a constraint annotation for validating the size of long values.
{@link LongSize} is a custom constraint annotation that can be applied to long fields, parameters, and methods. When a field, parameter, or method is annotated with @LongSize, it will be validated by the LongSizeValidation class to ensure that the length of the long value does not exceed the maximum value specified by the annotation.
The max() method returns the maximum size of the long value.
The message() method returns the error message to be displayed in case of validation failure.
The groups() and payload() method provide additional metadata about the constraint, and can be used by the validation framework during validation.
The annotation is applied at runtime and it targets method, field, and parameter.
@author Bootnext  team
@version 1.0
@since 2022-07
*/
@Documented
@Constraint(validatedBy = LongSizeValidation.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LongSize {

	String message() default "Length is greater than defined";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int max();

}
