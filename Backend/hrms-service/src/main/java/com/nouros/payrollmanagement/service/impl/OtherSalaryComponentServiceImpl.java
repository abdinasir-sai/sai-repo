package com.nouros.payrollmanagement.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enttribe.commons.configuration.ConfigUtils;
import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.payrollmanagement.integration.service.NamingIntegrationService;
import com.nouros.payrollmanagement.model.OtherSalaryComponent;
import com.nouros.payrollmanagement.model.OtherSalaryComponent.Type;
import com.nouros.payrollmanagement.repository.OtherSalaryComponentRepository;
import com.nouros.payrollmanagement.service.OtherSalaryComponentService;
import com.nouros.payrollmanagement.utils.PRConstant;

@Service
public class OtherSalaryComponentServiceImpl extends AbstractService<Integer,OtherSalaryComponent>
        implements OtherSalaryComponentService {

    private static final Logger log = LogManager.getLogger(OtherSalaryComponentServiceImpl.class);

	@Autowired
    private NamingIntegrationService namingIntegrationService;

    @Autowired
    private OtherSalaryComponentRepository otherSalaryComponentRepository;

    @Autowired
    public OtherSalaryComponentServiceImpl(GenericRepository<OtherSalaryComponent> repository) {
        super(repository, OtherSalaryComponent.class);
    }

    @TriggerBPMN(entityName = "OtherSalaryComponent", appName = "HRMS_APP_NAME")
    @Override
    public OtherSalaryComponent create(OtherSalaryComponent otherSalaryComponent) {
    	
        generateReferenceId(otherSalaryComponent);
        return otherSalaryComponentRepository.save(otherSalaryComponent);
    }

    public void generateReferenceId(OtherSalaryComponent otherSalaryComponent) {
    	log.info("Inside OtherSalaryComponentServiceImpl otherSalaryComponent  ");
        Map<String, String> parameters = new HashMap<>();
        String generatedName = "DFA";
        
        try {
        if (otherSalaryComponent !=null && otherSalaryComponent.getType() != null  && otherSalaryComponent.getEmployee()!=null) {
            String typeStr = otherSalaryComponent.getType().substring(0, 3).toUpperCase();
           
            parameters.put("part1", typeStr);
            String employeeId = otherSalaryComponent.getEmployee().getEmployeeId();
           
            log.info("Inside OtherSalaryComponentServiceImpl generateReferenceId  employeeId  : {}  type:{}" ,employeeId,typeStr);
            
            parameters.put("part2", employeeId);
            
            String rulename  = ConfigUtils.getString(PRConstant.OTHER_SALARY_RULE);
           
            log.info("Inside OtherSalaryComponentServiceImpl rule Name :{} " , rulename);
            
            generatedName = namingIntegrationService.generateNaming(rulename, parameters);
        }

        }catch(Exception ex) {
        	log.error("Inside OtherSalaryComponentServiceImpl generateReferenceId exception occured :{} ",ex.getMessage());
        }
        log.info("Generated Name :{} " , generatedName);
        otherSalaryComponent.setReferenceId(generatedName);
    }

    @Override
    public List<OtherSalaryComponent> findByEmployeeIdAndTypeAndDeleted(Integer employeeId,Type type,Boolean deleted) {
    	log.info("Inside OtherSalaryComponentServiceImpl findByEmployeeIdAndType  employeeId  : {}  type:{}" ,employeeId,type);
        List<OtherSalaryComponent> otherSalaryComponentList = 
        		otherSalaryComponentRepository.findByEmployeeIdAndTypeAndDeleted(employeeId,type.toString(),deleted);
        log.info("Inside OtherSalaryComponentServiceImpl findByEmployeeIdAndType EmployeeByIdAndDates : {} " , otherSalaryComponentList);
        return otherSalaryComponentList;
    }

	@Override
	public void softDelete(int id) {

		OtherSalaryComponent otherSalaryComponentopt = super.findById(id);

		if (otherSalaryComponentopt != null) {

			OtherSalaryComponent otherSalaryComponent = otherSalaryComponentopt;

			
			otherSalaryComponent.setDeleted(true);
			otherSalaryComponentRepository.save(otherSalaryComponent);
			
		}
	}
	
	
	@Override
	public void softBulkDelete(List<Integer> list) {
		log.debug( " OtherSalaryComponentServiceImpl softBulkDelete list :{} ", list);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				softDelete(list.get(i));
			}
		}

	}
}
