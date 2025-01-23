package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.ApplicantDependentDetails;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantDependentDetailsService extends GenericService<Integer, ApplicantDependentDetails>{

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
