/* (C)2024 */
package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.EmployeeCertification;
import com.nouros.hrms.service.generic.GenericService;

public interface EmployeeCertificationService extends GenericService<Integer, EmployeeCertification> {

	EmployeeCertification create(EmployeeCertification employeeCertification);

	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
