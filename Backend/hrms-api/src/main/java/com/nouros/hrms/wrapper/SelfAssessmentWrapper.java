package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.EmployeeCompetenciesDetails;
import com.nouros.hrms.model.EmployeeGoalsDetails;

import lombok.Data;

@Data
public class SelfAssessmentWrapper {

	List<EmployeeGoalsDetails> employeeGoalDetailsList;
	List<EmployeeCompetenciesDetails> employeeCompetenciesDetailsList;
    Integer employeeReviewId; 	
}
