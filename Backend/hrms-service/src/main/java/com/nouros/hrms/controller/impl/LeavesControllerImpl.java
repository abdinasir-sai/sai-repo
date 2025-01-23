package com.nouros.hrms.controller.impl;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.LeavesController;
import com.nouros.hrms.model.Leaves;
import com.nouros.hrms.service.LeavesService;
import com.nouros.hrms.wrapper.LeavesDto;
import com.nouros.hrms.wrapper.UnpaidLeaveWrapper;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the LeavesController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the LeavesController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the LeavesService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Leaves Leaves): creates an Leaves and returns the newly created Leaves.
count(String filter): returns the number of Leaves that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Leaves that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Leaves Leaves): updates an Leaves and returns the updated Leaves.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Leaves with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Leaves")
//@Tag(name="/Leaves",tags="Leaves",description="Leaves")
public class LeavesControllerImpl implements LeavesController {

  private static final Logger log = LogManager.getLogger(LeavesControllerImpl.class);

  @Autowired
  private LeavesService leavesService;
  

	
  @Override
  @TriggerBPMN(entityName = "Leaves", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE LEAVES")
  public Leaves create(Leaves leaves) {
	  log.info("inside @class LeavesControllerImpl @method create");
    return leavesService.create(leaves);
  }

  @Override
  public Long count(String filter) {
    return leavesService.count(filter);
  }

  @Override
  public List<Leaves> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return leavesService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE LEAVES")
  public Leaves update(Leaves leaves) {
    return leavesService.update(leaves);
  }

  @Override
  public Leaves findById(Integer id) {
    return leavesService.findById(id);
  }

  @Override
  public List<Leaves> findAllById(List<Integer> id) {
    return leavesService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE LEAVES")
  public void deleteById(Integer id) {
    leavesService.deleteById(id);
  }


	@Override
	public List<UnpaidLeaveWrapper> getUnpaidLeaveCount(Date fromDate, Date toDate, Integer employeeId) {
		
		return leavesService.getUnpaidLeaveCount( fromDate,  toDate,  employeeId);
	}

	@Override
	@Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE LEAVES BALANCE PROCESS INSTANCE ID")
	public String updateLeaveBalanceProcessInstanceId(@Valid String processInstanceId) {
		
		return leavesService.updateLeaveBalanceProcessInstanceId(processInstanceId);
	}

	@Override
	public Leaves updateLeavesWorkflowStage(LeavesDto leavesDto) {
		return leavesService.updateLeavesWorkflowStage(leavesDto);
	}

	
    @Override
    public String cancelLeaves(String processInstanceId)
    {
    	return leavesService.cancelLeaves(processInstanceId);
    }
		
    @Override
    public String deleteLeaveByLeaveId(Integer leaveId)
    {
    	return leavesService.deleteLeaveByLeaveId(leaveId);
    }
   
   
   
}
