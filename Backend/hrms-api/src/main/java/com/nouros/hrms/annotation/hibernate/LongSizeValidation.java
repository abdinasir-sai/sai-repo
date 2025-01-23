package com.nouros.hrms.annotation.hibernate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



/**

Class LongSizeValidation
Provides a constraint validator for the LongSize annotation.
{@link LongSizeValidation} is a class that implements the {@link ConstraintValidator} interface, which allows it to be used by the validation framework to validate long values annotated with the LongSize annotation.
The {@link #initialize(LongSize)} method is called when the validator is first applied, it assigns the max field with the value provided in the annotation and it also calls the super.initialize method
The {@link #isValid(Long, ConstraintValidatorContext)} method is called by the validation framework to check if the long value being validated meets the constraints defined in the LongSize annotation.
It converts the given Long value to String, and check the length of the string representation of the value against the max value, if the length of the string representation is less than or equal to max it returns true, else it returns false.
@author Bootnext  team
@version 1.0
@since 2022-07
*/
public class LongSizeValidation implements ConstraintValidator<LongSize, Long> {

	Integer max;
	/**
	Method initialize
	Initializes the validator for the LongSize constraint annotation.
	This method is called by the validation framework when the constraint is first applied, and should be used to initialize any resources needed for the validation process.
	@param constraintAnnotation the LongSize annotation that contains the constraints to be applied to the long value
	It assigns the max field with the value provided in the annotation and it also calls the super.initialize method
	@author Bootnext   team
	@version 1.0
	@since 2022-07
*/
	@Override
	public void initialize(LongSize constraintAnnotation) {
		max=constraintAnnotation.max();
		ConstraintValidator.super.initialize(constraintAnnotation);
	}


/**

Method isValid
Validates a long value against the constraints specified by the LongSize annotation.
This method is called by the validation framework to check if the long value being validated meets the constraints defined in the LongSize annotation.
@param value The long value to be validated
@param context The context in which the constraint is evaluated
It converts the given Long value to String, and check the length of the string representation of the value against the max value, if the length of the string representation is less than or equal to max it returns true, else it returns false.
@return true if the long value meets the constraints defined in the LongSize annotation, false otherwise
@author Bootnext  team
@version 1.0
@since 2022-07
*/
		@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		boolean success = false;
		if(null!=value)
		{
		String size=value.toString();
	    if(  size.length() <= max){
	      success = true;
	    } else {
	      success = false;
	    }
		}
	    return success;
	}

}
