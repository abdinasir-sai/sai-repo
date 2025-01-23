package com.nouros.hrms.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.utils.Utils;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.EmployeeSuccessor;
import com.nouros.hrms.model.Successor;
import com.nouros.hrms.repository.SuccessorRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.EmployeeSuccessorService;
import com.nouros.hrms.service.SuccessorService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.APIConstants;

@Service
public class SuccessorServiceImpl extends AbstractService<Integer,Successor> implements SuccessorService {

	public SuccessorServiceImpl(GenericRepository<Successor> repository) {
		super(repository, Successor.class);
	}

	@Autowired
	private SuccessorRepository successorRepository;
	
	private static final Logger log = LogManager.getLogger(EmployeeSuccessorServiceImpl.class);

	@Override
   public String setRecommendedCandidate(Integer employeeSuccessorId)
   {
	   EmployeeSuccessorService employeeSuccessorService = ApplicationContextProvider.getApplicationContext().getBean(EmployeeSuccessorService.class);
	   try
	   {
		   log.debug("Inside @class SuccessorServiceImpl @method setRecommendedCandidate employeeSuccessorId :{} ",employeeSuccessorId);
		   EmployeeSuccessor employeeSuccessor = employeeSuccessorService.findById(employeeSuccessorId);
		   Employee employee =  employeeSuccessor.getEmployee();
		   Successor successor =  employeeSuccessor.getSuccessor();
		   successor.setEmployee(employee);
		   successorRepository.save(successor);
		   return APIConstants.SUCCESS_JSON;
	   }
	   catch(Exception e)
	   {
		   log.error("Error inside @class SuccessorServiceImpl @method setRecommendedCandidate :{} :{} ",e.getMessage(),Utils.getStackTrace(e));
		   throw new BusinessException();
	   }
   }

}
