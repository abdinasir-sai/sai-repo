package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.CompensationStructureController;
import com.nouros.hrms.model.CompensationStructure;
import com.nouros.hrms.service.CompensationStructureService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the CompensationStructureController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the CompensationStructureController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the CompensationStructureService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(CompensationStructure CompensationStructure): creates an CompensationStructure and returns the newly created CompensationStructure.
count(String filter): returns the number of CompensationStructure that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of CompensationStructure that match the specified filter, sorted according to the specified orderBy
and orderType.
update(CompensationStructure CompensationStructure): updates an CompensationStructure and returns the updated CompensationStructure.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of CompensationStructure with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/CompensationStructure")

//@Tag(name="/CompensationStructure",tags="CompensationStructure",description="CompensationStructure")
public class CompensationStructureControllerImpl implements CompensationStructureController {

  private static final Logger log = LogManager.getLogger(CompensationStructureControllerImpl.class);

  @Autowired
  private CompensationStructureService compensationStructureService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public CompensationStructure create(CompensationStructure compensationStructure) {
	  log.info("inside @class CompensationStructureControllerImpl @method create");
    return compensationStructureService.create(compensationStructure);
  }

  @Override
  public Long count(String filter) {
    return compensationStructureService.count(filter);
  }

  @Override
  public List<CompensationStructure> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return compensationStructureService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public CompensationStructure update(CompensationStructure compensationStructure) {
    return compensationStructureService.update(compensationStructure);
  }

  @Override
  public CompensationStructure findById(Integer id) {
    return compensationStructureService.findById(id);
  }

  @Override
  public List<CompensationStructure> findAllById(List<Integer> id) {
    return compensationStructureService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    compensationStructureService.deleteById(id);
  }
  

	@Override
	public CompensationStructure findByTitle(String title) {
		return compensationStructureService.findByTitle(title);
	}

		
   
   
   
   
}
