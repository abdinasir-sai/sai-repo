package com.nouros.hrms.wrapper;

import java.util.List;
import java.util.Map;

import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.OrgChartDesignation;

import lombok.Data;

@Data
public class JobOpeningAndApplicantReferralDto {
	
	OrgChartDesignation designation;
	Boolean jobRequisition;
	Boolean headHunted;
	List<Map<String, String>> referralList;
    String city;
    List<Map<String, String>> configurationList;
    Employee hiringManager;
}
