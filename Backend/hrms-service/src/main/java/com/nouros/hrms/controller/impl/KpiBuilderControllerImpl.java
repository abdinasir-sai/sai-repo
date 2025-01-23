package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.KpiBuilderController;
import com.nouros.hrms.model.KpiBuilder;
import com.nouros.hrms.service.KpiBuilderService;
import com.nouros.hrms.wrapper.KpiCardDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the KpiBuilderController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the KpiBuilderController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the KpiBuilderService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(KpiBuilder KpiBuilder): creates an KpiBuilder and returns the newly created KpiBuilder.
count(String filter): returns the number of KpiBuilder that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of KpiBuilder that match the specified filter, sorted according to the specified orderBy
and orderType.
update(KpiBuilder KpiBuilder): updates an KpiBuilder and returns the updated KpiBuilder.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of KpiBuilder with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/KpiBuilder")
//@Tag(name="/KpiBuilder",tags="KpiBuilder",description="KpiBuilder")
public class KpiBuilderControllerImpl implements KpiBuilderController {

  private static final Logger log = LogManager.getLogger(KpiBuilderControllerImpl.class);

  @Autowired
  private KpiBuilderService kpiBuilderService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public KpiBuilder create(KpiBuilder kpiBuilder) {
	  log.info("inside @class KpiBuilderControllerImpl @method create");
    return kpiBuilderService.create(kpiBuilder);
  }

  @Override
  public Long count(String filter) {
    return kpiBuilderService.count(filter);
  }

  @Override
  public List<KpiBuilder> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return kpiBuilderService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public KpiBuilder update(KpiBuilder kpiBuilder) {
    return kpiBuilderService.update(kpiBuilder);
  }

  @Override
  public KpiBuilder findById(Integer id) {
    return kpiBuilderService.findById(id);
  }

  @Override
  public List<KpiBuilder> findAllById(List<Integer> id) {
    return kpiBuilderService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    kpiBuilderService.deleteById(id);
  }


	@Override
	public List<KpiCardDto> getKPICardByRole() {
		return kpiBuilderService.getKPICardByRole();
	}

		
   
   
   
   
}
