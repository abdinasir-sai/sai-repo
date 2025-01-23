package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.TravelExpenseController;
import com.nouros.hrms.model.TravelExpense;
import com.nouros.hrms.service.TravelExpenseService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the TravelExpenseController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the TravelExpenseController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the TravelExpenseService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(TravelExpense TravelExpense): creates an TravelExpense and returns the newly created TravelExpense.
count(String filter): returns the number of TravelExpense that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of TravelExpense that match the specified filter, sorted according to the specified orderBy
and orderType.
update(TravelExpense TravelExpense): updates an TravelExpense and returns the updated TravelExpense.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of TravelExpense with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/TravelExpense")
//@Tag(name="/TravelExpense",tags="TravelExpense",description="TravelExpense")
public class TravelExpenseControllerImpl implements TravelExpenseController {

  private static final Logger log = LogManager.getLogger(TravelExpenseControllerImpl.class);

  @Autowired
  private TravelExpenseService travelExpenseService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public TravelExpense create(TravelExpense travelExpense) {
	  log.info("inside @class TravelExpenseControllerImpl @method create");
    return travelExpenseService.create(travelExpense);
  }

  @Override
  public Long count(String filter) {
    return travelExpenseService.count(filter);
  }

  @Override
  public List<TravelExpense> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return travelExpenseService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public TravelExpense update(TravelExpense travelExpense) {
    return travelExpenseService.update(travelExpense);
  }

  @Override
  public TravelExpense findById(Integer id) {
    return travelExpenseService.findById(id);
  }

  @Override
  public List<TravelExpense> findAllById(List<Integer> id) {
    return travelExpenseService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    travelExpenseService.deleteById(id);
  }
  


		
   
   
   
   
}
