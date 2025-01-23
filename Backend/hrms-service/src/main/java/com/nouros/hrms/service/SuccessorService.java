package com.nouros.hrms.service;

import com.nouros.hrms.model.Successor;
import com.nouros.hrms.service.generic.GenericService;

public interface SuccessorService extends GenericService<Integer,Successor> {

	String setRecommendedCandidate(Integer employeeSuccessorId);
}
