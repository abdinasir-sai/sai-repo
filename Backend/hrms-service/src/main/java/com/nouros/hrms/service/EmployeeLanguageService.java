/* (C)2024 */
package com.nouros.hrms.service;

import com.nouros.hrms.model.EmployeeLanguage;
import com.nouros.hrms.service.generic.GenericService;

// import com.nouros.hrms.service.generic.GenericService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * EmployeeLanguageService interface is a service layer interface which handles all the business logic related to EmployeeLanguage model.
 * It extends GenericService interface which provides basic CRUD operations.
 * @author Visionwaves EmployeeLanguage
 * @version 1.0.0
 *
 **/
public interface EmployeeLanguageService extends GenericService<Integer, EmployeeLanguage> {

    
    EmployeeLanguage create(EmployeeLanguage employeeLanguage);
	
    void softDelete(int id);

    void softBulkDelete(List<Integer> list);
}
