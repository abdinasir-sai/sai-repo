package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.EmployeeGoalsDetails;

import lombok.Data;

@Data
public class GoalWrapper {

	Integer employeeReviewId;
	List<EmployeeGoalsDetails> employeeGoalDetailList; 
}
