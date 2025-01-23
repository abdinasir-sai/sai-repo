package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.ApplicantReferral;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantReferralService extends GenericService<Integer,ApplicantReferral>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public ApplicantReferral create(ApplicantReferral applicantReferral);

}
