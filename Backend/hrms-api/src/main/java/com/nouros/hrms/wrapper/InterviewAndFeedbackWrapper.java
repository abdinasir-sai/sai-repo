package com.nouros.hrms.wrapper;

import java.util.List;

import com.nouros.hrms.model.Interview;
import com.nouros.hrms.model.InterviewFeedback;

import lombok.Data;

@Data
public class InterviewAndFeedbackWrapper {

	Interview interview;
	List<InterviewFeedback> interviewFeedbacks;
	
}
