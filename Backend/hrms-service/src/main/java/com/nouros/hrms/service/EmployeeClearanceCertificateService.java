/* (C)2024 */
package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeClearanceCertificate;
import com.nouros.hrms.service.generic.GenericService;

public interface EmployeeClearanceCertificateService extends GenericService<Integer, EmployeeClearanceCertificate> {

	EmployeeClearanceCertificate create(EmployeeClearanceCertificate employeeClearanceCertificate);
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
