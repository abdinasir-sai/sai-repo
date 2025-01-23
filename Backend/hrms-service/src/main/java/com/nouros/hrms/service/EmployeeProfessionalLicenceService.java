/* (C)2024 */
package com.nouros.hrms.service;

import com.nouros.hrms.model.EmployeeProfessionalLicence;
import com.nouros.hrms.service.generic.GenericService;

// import com.nouros.hrms.service.generic.GenericService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface EmployeeProfessionalLicenceService extends GenericService<Integer, EmployeeProfessionalLicence> {

	
	EmployeeProfessionalLicence create(EmployeeProfessionalLicence employeeProfessionalLicence);
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);
}
