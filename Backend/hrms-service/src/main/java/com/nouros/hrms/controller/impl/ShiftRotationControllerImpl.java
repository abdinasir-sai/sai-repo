package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.ShiftRotationController;
import com.nouros.hrms.model.ShiftRotation;
import com.nouros.hrms.service.ShiftRotationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the ShiftRotationController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the ShiftRotationController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the ShiftRotationService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(ShiftRotation ShiftRotation): creates an ShiftRotation and returns the newly created ShiftRotation.
count(String filter): returns the number of ShiftRotation that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of ShiftRotation that match the specified filter, sorted according to the specified orderBy
and orderType.
update(ShiftRotation ShiftRotation): updates an ShiftRotation and returns the updated ShiftRotation.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of ShiftRotation with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/ShiftRotation")
//@Tag(name="/ShiftRotation",tags="ShiftRotation",description="ShiftRotation")
public class ShiftRotationControllerImpl implements ShiftRotationController {


  private static final Logger log = LogManager.getLogger(ShiftRotationControllerImpl.class);
  @Autowired
  private ShiftRotationService shiftRotationService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public ShiftRotation create(ShiftRotation shiftRotation) {
	  log.info("inside @class ShiftRotationControllerImpl @method create");
    return shiftRotationService.create(shiftRotation);
  }

  @Override
  public Long count(String filter) {
    return shiftRotationService.count(filter);
  }

  @Override
  public List<ShiftRotation> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return shiftRotationService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public ShiftRotation update(ShiftRotation shiftRotation) {
    return shiftRotationService.update(shiftRotation);
  }

  @Override
  public ShiftRotation findById(Integer id) {
    return shiftRotationService.findById(id);
  }

  @Override
  public List<ShiftRotation> findAllById(List<Integer> id) {
    return shiftRotationService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    shiftRotationService.deleteById(id);
  }
  


   
   
   
}
