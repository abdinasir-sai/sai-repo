package com.nouros.payrollmanagement.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryPayrollMapping;
import com.nouros.payrollmanagement.repository.OtherSalaryPayrollMappingRepository;
import com.nouros.payrollmanagement.service.OtherSalaryPayrollMappingService;


@Service
public class OtherSalaryPayrollMappingServiceImpl extends AbstractService<Integer,OtherSalaryPayrollMapping>
implements OtherSalaryPayrollMappingService {

	 private static final Logger log = LogManager.getLogger(OtherSalaryPayrollMappingServiceImpl.class);
	
	@Autowired
    public OtherSalaryPayrollMappingServiceImpl(GenericRepository<OtherSalaryPayrollMapping> repository) {
        super(repository, OtherSalaryPayrollMapping.class);
    }

	@Override
	public List<OtherSalaryComponent> getComponentsByPayrollId(
			Integer payrollRunId, String type, Boolean deleted) {
		log.info("Inside OtherSalaryPayrollMappingServiceImpl @method findByPayrollRunIdAndOtherSalaryComponentTypeAndOtherSalaryComponentDeleted payrollRunId:{}  type: {} deleted :{} ",payrollRunId,type ,deleted);	
		OtherSalaryPayrollMappingRepository otherSalaryPayrollMappingRepository = ApplicationContextProvider.getApplicationContext().getBean(OtherSalaryPayrollMappingRepository.class);
		
		return otherSalaryPayrollMappingRepository.findByPayrollRunIdAndOtherSalaryComponentTypeAndOtherSalaryComponentDeleted(payrollRunId, type, deleted);
	
	}

	@Override
	public OtherSalaryPayrollMapping getMappingAmount(Integer othersalaryComponentId, Boolean deleted) {
		log.info("Inside OtherSalaryPayrollMappingServiceImpl @method getMappingAmount othersalaryComponentId:{}  deleted :{} ",othersalaryComponentId ,deleted);	
		OtherSalaryPayrollMappingRepository otherSalaryPayrollMappingRepository = ApplicationContextProvider.getApplicationContext().getBean(OtherSalaryPayrollMappingRepository.class);
		List<OtherSalaryPayrollMapping> list = otherSalaryPayrollMappingRepository.findMapping(othersalaryComponentId, deleted);
		if(list!=null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
