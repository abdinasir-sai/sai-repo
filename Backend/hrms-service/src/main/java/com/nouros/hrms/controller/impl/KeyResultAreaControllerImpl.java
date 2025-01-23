package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.KeyResultAreaController;
import com.nouros.hrms.model.KeyResultArea;
import com.nouros.hrms.service.KeyResultAreaService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the KeyResultAreaController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the KeyResultAreaController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the KeyResultAreaService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(KeyResultArea KeyResultArea): creates an KeyResultArea and returns the newly created KeyResultArea.
count(String filter): returns the number of KeyResultArea that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of KeyResultArea that match the specified filter, sorted according to the specified orderBy
and orderType.
update(KeyResultArea KeyResultArea): updates an KeyResultArea and returns the updated KeyResultArea.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of KeyResultArea with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/KeyResultArea")
//@Tag(name="/KeyResultArea",tags="KeyResultArea",description="KeyResultArea")
public class KeyResultAreaControllerImpl implements KeyResultAreaController {

  private static final Logger log = LogManager.getLogger(KeyResultAreaControllerImpl.class);

  @Autowired
  private KeyResultAreaService keyResultAreaService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public KeyResultArea create(KeyResultArea keyResultArea) {
	  log.info("inside @class KeyResultAreaControllerImpl @method create");
    return keyResultAreaService.create(keyResultArea);
  }

  @Override
  public Long count(String filter) {
    return keyResultAreaService.count(filter);
  }

  @Override
  public List<KeyResultArea> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return keyResultAreaService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public KeyResultArea update(KeyResultArea keyResultArea) {
    return keyResultAreaService.update(keyResultArea);
  }

  @Override
  public KeyResultArea findById(Integer id) {
    return keyResultAreaService.findById(id);
  }

  @Override
  public List<KeyResultArea> findAllById(List<Integer> id) {
    return keyResultAreaService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    keyResultAreaService.deleteById(id);
  }
  


   
}
