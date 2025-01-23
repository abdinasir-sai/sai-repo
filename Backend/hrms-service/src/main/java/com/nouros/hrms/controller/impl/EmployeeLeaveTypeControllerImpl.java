package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeLeaveTypeController;
import com.nouros.hrms.model.EmployeeLeaveType;
import com.nouros.hrms.model.LeaveType;
import com.nouros.hrms.service.EmployeeLeaveTypeService;
import com.nouros.hrms.service.LeaveTypeService;
import com.nouros.hrms.util.report.CommonUtils;
import com.nouros.hrms.wrapper.LeaveValidationDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeLeaveTypeController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeLeaveTypeController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeLeaveTypeService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeLeaveType EmployeeLeaveType): creates an EmployeeLeaveType and returns the newly created EmployeeLeaveType.
count(String filter): returns the number of EmployeeLeaveType that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeLeaveType that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeLeaveType EmployeeLeaveType): updates an EmployeeLeaveType and returns the updated EmployeeLeaveType.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeLeaveType with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeLeaveType")
//@Tag(name="/EmployeeLeaveType",tags="EmployeeLeaveType",description="EmployeeLeaveType")
public class EmployeeLeaveTypeControllerImpl implements EmployeeLeaveTypeController {

  private static final Logger log = LogManager.getLogger(EmployeeLeaveTypeControllerImpl.class);

  @Autowired
  private EmployeeLeaveTypeService employeeLeaveTypeService;
  
  @Autowired
  private CommonUtils commonUtils;

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public EmployeeLeaveType create(EmployeeLeaveType employeeLeaveType) {
    return employeeLeaveTypeService.create(employeeLeaveType);
  }

  @Override
  public Long count(String filter) {
    return employeeLeaveTypeService.count(filter);
  }

  @Override
  public List<EmployeeLeaveType> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeLeaveTypeService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public EmployeeLeaveType update(EmployeeLeaveType employeeLeaveType) {
    return employeeLeaveTypeService.update(employeeLeaveType);
  }

  @Override
  public EmployeeLeaveType findById(Integer id) {
    return employeeLeaveTypeService.findById(id);
  }

  @Override
  public List<EmployeeLeaveType> findAllById(List<Integer> id) {
    return employeeLeaveTypeService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    employeeLeaveTypeService.deleteById(id);
  }
  

    
@Override
public String leaveValidate(LeaveValidationDto leaveValidation) {
	log.info("inside @class EmployeeLeaveTypeControllerImpl @method leaveValidate");
	log.debug("inside @class EmployeeLeaveTypeControllerImpl @method leaveValidate :{}", leaveValidation);
	try {
		Integer leaveTypeid = Integer.parseInt(leaveValidation.getLeaveTypeId());
		LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext()
				.getBean(LeaveTypeService.class);
		LeaveType optionalLeaveType = leaveTypeService.findById(leaveTypeid);
		if (optionalLeaveType != null) {
			LeaveType leaveTypeObj = optionalLeaveType;
		return employeeLeaveTypeService.leaveValidate(leaveValidation.getEmpId(), leaveTypeid,
				leaveValidation.getFromDate(), leaveValidation.getToDate(),leaveValidation.getDuration(),leaveTypeObj);
		}
		else {
			throw new BusinessException("Not Valid Leave Type ");
		}
		} catch (NumberFormatException e) {
		LeaveTypeService leaveTypeService = ApplicationContextProvider.getApplicationContext()
				.getBean(LeaveTypeService.class);
		LeaveType leaveType = leaveTypeService.findByName(leaveValidation.getLeaveTypeId());
		if (leaveType != null) {
			return employeeLeaveTypeService.leaveValidate(leaveValidation.getEmpId(), leaveType.getId(),
					leaveValidation.getFromDate(), leaveValidation.getToDate(),leaveValidation.getDuration(),leaveType);
		} else {
			throw new BusinessException("No data found for given leave type");
		}
	}
}

	@Override
	public String creaditLeaveByEmpId(Integer empId) {
		log.info("inside @class EmployeeLeaveTypeControllerImpl @method creditLeaveByEmpId");
		log.debug("inside @class EmployeeLeaveTypeControllerImpl @method creditLeaveByEmpId :{}", empId);
		return employeeLeaveTypeService.creaditLeaveByEmpId(empId);
		
	}

	@Override
	public void creaditLeave() {
		 employeeLeaveTypeService.creaditLeave();		
	}

	@Override
	public void updateLeaveBalanceWhenYearComplete() {
		employeeLeaveTypeService.updateLeaveBalanceWhenYearComplete();		
	}
		
   
   
   
   
}
