package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.EmployeeDependentDetailsController;
import com.nouros.hrms.model.EmployeeDependentDetails;
import com.nouros.hrms.service.EmployeeDependentDetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the EmployeeDependentDetailsController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the EmployeeDependentDetailsController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the EmployeeDependentDetailsService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(EmployeeDependentDetails EmployeeDependentDetails): creates an EmployeeDependentDetails and returns the newly created EmployeeDependentDetails.
count(String filter): returns the number of EmployeeDependentDetails that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of EmployeeDependentDetails that match the specified filter, sorted according to the specified orderBy
and orderType.
update(EmployeeDependentDetails EmployeeDependentDetails): updates an EmployeeDependentDetails and returns the updated EmployeeDependentDetails.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of EmployeeDependentDetails with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/EmployeeDependentDetails")
//@Tag(name="/EmployeeDependentDetails",tags="EmployeeDependentDetails",description="EmployeeDependentDetails")
public class EmployeeDependentDetailsControllerImpl implements EmployeeDependentDetailsController {

  private static final Logger log = LogManager.getLogger(EmployeeDependentDetailsControllerImpl.class);

  @Autowired
  private EmployeeDependentDetailsService employeeDependentDetailsService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE EMPLOYEE DEPENDENT DETAILS")
  public EmployeeDependentDetails create(EmployeeDependentDetails employeeDependentDetails) {
	  log.info("inside @class EmployeeDependentDetailsControllerImpl @method create");
    return employeeDependentDetailsService.create(employeeDependentDetails);
  }

  @Override
  public Long count(String filter) {
    return employeeDependentDetailsService.count(filter);
  }

  @Override
  public List<EmployeeDependentDetails> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return employeeDependentDetailsService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE EMPLOYEE DEPENDENT DETAILS")
  public EmployeeDependentDetails update(EmployeeDependentDetails employeeDependentDetails) {
    return employeeDependentDetailsService.update(employeeDependentDetails);
  }

  @Override
  public EmployeeDependentDetails findById(Integer id) {
    return employeeDependentDetailsService.findById(id);
  }

  @Override
  public List<EmployeeDependentDetails> findAllById(List<Integer> id) {
    return employeeDependentDetailsService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE EMPLOYEE DEPENDENT DETAILS")
  public void deleteById(Integer id) {
    employeeDependentDetailsService.deleteById(id);
  }

	@Override
	public List<EmployeeDependentDetails>  getSelfEmployeeDependentDetails(Integer id,Integer userId) {
		return employeeDependentDetailsService.getSelfEmployeeDependentDetails(id,userId);
	}
	
	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE SELF EMPLOYEE DEPENDENT DETAILS")
	public List<EmployeeDependentDetails> updateSelfEmployeeDependentDetails(List<EmployeeDependentDetails> employeeDependentDetailsList) {
		return employeeDependentDetailsService.updateSelfEmployeeDependentDetails(employeeDependentDetailsList);
	}

		
   
   
   
   
}
