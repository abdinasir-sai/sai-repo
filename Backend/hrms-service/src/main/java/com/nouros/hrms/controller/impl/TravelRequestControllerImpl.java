package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.TravelRequestController;
import com.nouros.hrms.model.TravelRequest;
import com.nouros.hrms.service.TravelRequestService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the TravelRequestController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the TravelRequestController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the TravelRequestService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(TravelRequest TravelRequest): creates an TravelRequest and returns the newly created TravelRequest.
count(String filter): returns the number of TravelRequest that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of TravelRequest that match the specified filter, sorted according to the specified orderBy
and orderType.
update(TravelRequest TravelRequest): updates an TravelRequest and returns the updated TravelRequest.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of TravelRequest with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/TravelRequest")
//@Tag(name="/TravelRequest",tags="TravelRequest",description="TravelRequest")
public class TravelRequestControllerImpl implements TravelRequestController {

  private static final Logger log = LogManager.getLogger(TravelRequestControllerImpl.class);

  @Autowired
  private TravelRequestService travelRequestService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public TravelRequest create(TravelRequest travelRequest) {
	  log.info("inside @class TravelRequestControllerImpl @method create");
    return travelRequestService.create(travelRequest);
  }

  @Override
  public Long count(String filter) {
    return travelRequestService.count(filter);
  }

  @Override
  public List<TravelRequest> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return travelRequestService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public TravelRequest update(TravelRequest travelRequest) {
    return travelRequestService.update(travelRequest);
  }

  @Override
  public TravelRequest findById(Integer id) {
    return travelRequestService.findById(id);
  }

  @Override
  public List<TravelRequest> findAllById(List<Integer> id) {
    return travelRequestService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    travelRequestService.deleteById(id);
  }

		
   
   
   
   
}
