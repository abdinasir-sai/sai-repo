package com.nouros.hrms.wrapper;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.City;
import com.nouros.hrms.model.Country;

import lombok.Data;

@Data
public class ApplicantWrapper {

    String fullName;
    String phone;
	City city;
	Country country;
	String currentEmployer;
	String currentJobTitle;
	String currentSalary;
	String emailId;
	String expectedSalary;
	String experienceInYears;
    String firstName;
    String lastName;
    String highestQualification;
	String mobile;
	String skillSet;
	String street;
	String presentAddress;
	
   List<Map<String, String>> applicantEducationList;
   List<Map<String, String>> applicantLanguageList;
	
}
