package com.nouros.hrms.controller.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.SkillController;
import com.nouros.hrms.model.Skill;
import com.nouros.hrms.service.SkillService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the SkillController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the SkillController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the SkillService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Skill Skill): creates an Skill and returns the newly created Skill.
count(String filter): returns the number of Skill that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Skill that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Skill Skill): updates an Skill and returns the updated Skill.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Skill with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Skill")
//@Tag(name="/Skill",tags="Skill",description="Skill")
public class SkillControllerImpl implements SkillController {

  private static final Logger log = LogManager.getLogger(SkillControllerImpl.class);

  @Autowired
  private SkillService skillService;
  

	
  @Override
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Skill create(Skill skill) {
	  log.info("inside @class SkillControllerImpl @method create");
    return skillService.create(skill);
  }

  @Override
  public Long count(String filter) {
    return skillService.count(filter);
  }

  @Override
  public List<Skill> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return skillService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Skill update(Skill skill) {
    return skillService.update(skill);
  }

  @Override
  public Skill findById(Integer id) {
    return skillService.findById(id);
  }

  @Override
  public List<Skill> findAllById(List<Integer> id) {
    return skillService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    skillService.deleteById(id);
  }
  
   
   
   
   
}
