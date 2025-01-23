package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.HolidayController;
import com.nouros.hrms.model.Holiday;
import com.nouros.hrms.service.HolidayService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the HolidayController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the HolidayController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the HolidayService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Holiday Holiday): creates an Holiday and returns the newly created Holiday.
count(String filter): returns the number of Holiday that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Holiday that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Holiday Holiday): updates an Holiday and returns the updated Holiday.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Holiday with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Holiday")
//@Tag(name="/Holiday",tags="Holiday",description="Holiday")
public class HolidayControllerImpl implements HolidayController {


  private static final Logger log = LogManager.getLogger(HolidayControllerImpl.class);

  @Autowired
  private HolidayService holidayService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Holiday create(Holiday holiday) {
	  log.info("inside @class HolidayControllerImpl @method create");
    return holidayService.create(holiday);
  }

  @Override
  public Long count(String filter) {
    return holidayService.count(filter);
  }

  @Override
  public List<Holiday> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return holidayService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Holiday update(Holiday holiday) {
    return holidayService.update(holiday);
  }

  @Override
  public Holiday findById(Integer id) {
    return holidayService.findById(id);
  }

  @Override
  public List<Holiday> findAllById(List<Integer> id) {
    return holidayService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    holidayService.deleteById(id);
  }
  
  @Override
  public String createWeekendHolidayForYear()
  {
	 return holidayService.createWeekendHolidayForYear();
  }
   
   
   
   
}
