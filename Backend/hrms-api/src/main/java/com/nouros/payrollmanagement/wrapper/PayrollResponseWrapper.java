package com.nouros.payrollmanagement.wrapper;

import java.util.Map;

import lombok.Data;



@Data
public class PayrollResponseWrapper {

	
	Integer sucessCount;
	
	Integer failureCount;
	
	Map<Object,Object> responseList;
	
}
