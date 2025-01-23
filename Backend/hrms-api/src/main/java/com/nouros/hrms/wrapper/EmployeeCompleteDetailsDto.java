package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeComplianceLegal;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.model.EmployeeEmergencyContact;
import com.nouros.hrms.model.EmployeeNationalIdentification;

import lombok.Data;

@Data
public class EmployeeCompleteDetailsDto {
	
	private Employee employee;
	private List<EmployeeDependentDetails> dependentDetails;
	private EmployeeEmergencyContact emergencyDetails; 
	private EmployeeComplianceLegal complianceDetails;
	private EmployeeNationalIdentification nationalIdentificationDetails;

}
