package com.nouros.hrms.wrapper;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDetailsDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private String givenName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String personalEmailAddress;
	private String personalMobileNumber;
	private String workPhoneNumber;
	private String maritalStatus;
	private String countryOfResidence;
	private String religion;
	private String primaryAddressBuildingNumber;
	private String primaryAddressCity;
	private String primaryAddressCountry;
	private String primaryAddressPostalCode;
	private String primaryShortAddress;
	private String bloodGroup;
	private String arabicFirstName;
	private String arabicMiddleName;
	private String arabicLastName;
	private Integer reportingManagersUserId;
	private String reportingManagersFullName;
	private List<EmployeeDependentWrapper> dependentDetails;
    private EmployeeEmergencyWrapper emergencyDetails;
    private EmployeeComplianceWrapper employeeCompliance;
    private EmployeeNationalWrapper employeeNational;
}
