package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.ApplicantNationalIdentification;
import com.nouros.hrms.service.generic.GenericService;

public interface ApplicantNationalIdentificationService extends GenericService<Integer, ApplicantNationalIdentification> {

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
