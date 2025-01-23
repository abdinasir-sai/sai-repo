package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class NDARequestDto {
	
	Integer templateId;
	String companyName;
	String companyAddress;
	String scopeOfProject;
	String projectBudget;
	String durationOfConfidentiality;
	Integer id;
	String clouseNameToBeAdded;
	String existingClouseName;
	String clousePosition;
	
	
}
