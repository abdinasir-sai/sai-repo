package com.nouros.hrms.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nouros.hrms.model.EmployeeLanguage;
import com.nouros.hrms.repository.EmployeeLanguageRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeLanguageService;
import com.nouros.hrms.service.generic.AbstractService;


@Service
public class EmployeeLanguageServiceImpl extends AbstractService<Integer, EmployeeLanguage>
        implements EmployeeLanguageService {
	
	
	private static final Logger log = LogManager.getLogger(EmployeeLanguageServiceImpl.class);

    public EmployeeLanguageServiceImpl(GenericRepository<EmployeeLanguage> repository) {
        super(repository, EmployeeLanguage.class);
    }


    @Autowired private EmployeeLanguageRepository employeeLanguageRepository;

    
    @Override
    public void softDelete(int id) {
        Optional<EmployeeLanguage> employeeLanguage = employeeLanguageRepository.findById(id);
        if (employeeLanguage.isPresent()) {
            EmployeeLanguage employeeLanguage1 = employeeLanguage.get();
            employeeLanguage1.setDeleted(true);
            employeeLanguageRepository.save(employeeLanguage1);
        }
    }

  
    @Override
    public void softBulkDelete(List<Integer> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                softDelete(list.get(i));
            }
        }
    }

 
    @Override
    public EmployeeLanguage create(EmployeeLanguage employeeLanguage) {
    	log.info("Inside @class EmployeeLanguageServiceImpl @method create");
        return employeeLanguageRepository.save(employeeLanguage);
    }
}
