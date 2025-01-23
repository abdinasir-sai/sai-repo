package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.nouros.hrms.controller.OrgChartDesignationController;
import com.nouros.hrms.model.Employee;
import com.nouros.hrms.model.OrgChartDesignation;
import com.nouros.hrms.repository.OrgChartDesignationRepository;
import com.nouros.hrms.service.OrgChartDesignationService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.DesignationSummaryWrapper;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/OrgChartDesignation")
public class OrgChartDesignationControllerImpl implements OrgChartDesignationController {

	private static final Logger log = LogManager.getLogger(OrgChartDesignationControllerImpl.class);
	
	@Autowired
	  private OrgChartDesignationService orgChartDesignationService;
	
	@Autowired
	private OrgChartDesignationRepository orgChartDesignationRepository; 
	
	@Autowired
	  private CommonUtils commonUtils;
	
	@Override
	  public OrgChartDesignation create(OrgChartDesignation orgChartDesignation) {
		  log.info("inside @class OrgChartDesignationController @method create");
	    return orgChartDesignationService.create(orgChartDesignation);
	  }

	  
	  @Override
	  public Long count(String filter) {
	    return orgChartDesignationService.count(filter);
	  }

	  @Override
	  public List<OrgChartDesignation> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
	    return orgChartDesignationService.search(filter, offset, size, orderBy, orderType);
	  }

	  @Override
	  public OrgChartDesignation update(OrgChartDesignation orgChartDesignation) {
	    return orgChartDesignationService.update(orgChartDesignation);
	  }

	  @Override
	  public OrgChartDesignation findById(Integer id) {
	    return orgChartDesignationService.findById(id);
	  }

	  @Override
	  public List<OrgChartDesignation> findAllById(List<Integer> id) {
	    return orgChartDesignationService.findAllById(id);
	  }

	  @Override
	  public void deleteById(Integer id) {
		  orgChartDesignationService.deleteById(id);
	  }


	@Override
	public String deleteOrgChartDesignation(Integer id, Integer departmentId) {
		try {
		if (id != null && id >0) {
			 OrgChartDesignation orgChartDesignation = orgChartDesignationService.findById(id);
		        if (orgChartDesignation == null) {
		            throw new BusinessException("OrgChartDesignation with ID  does not exist");
		        }
            return orgChartDesignationService.deleteOrgChartDesignationById(id);
        } else if (departmentId != null && departmentId >0 ) {
        	 List<OrgChartDesignation> designations = orgChartDesignationRepository.findByDepartmentId(departmentId,commonUtils.getCustomerId());
             if (designations.isEmpty()) {
                 throw new BusinessException("No designations found for department ID ");
             }
            return orgChartDesignationService.deleteOrgChartDesignationByDepartmentId(departmentId);
        } else {
            throw new BusinessException("Please provide either 'id' or 'departmentId'");
        }
		}catch (BusinessException ex) {
			throw new BusinessException("error inside deleteOrgChartDesignation in class OrgChartDesignationControllerImpl",ex.getMessage());
		}
	}


	@Override
	public String generateJobTitleDescriptionByUserInput(DesignationSummaryWrapper designationSummaryWrapper) {
	return orgChartDesignationService.generateJobTitleDescriptionByUserInput(designationSummaryWrapper);
	}


	@Override
	public List<Employee> getEmployeeByApprovedDesignation(Integer id) {
		return orgChartDesignationService.getEmployeeByApprovedDesignation(id) ;
	}



	
}
