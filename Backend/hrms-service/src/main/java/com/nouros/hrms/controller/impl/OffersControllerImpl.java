package com.nouros.hrms.controller.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.orchestrator.utility.annotation.TriggerBPMN;
import com.enttribe.product.audit.utils.ActionType;
import com.enttribe.product.audit.utils.Auditable;
import com.nouros.hrms.controller.OffersController;
import com.nouros.hrms.model.Offers;
import com.nouros.hrms.service.OffersService;
import com.nouros.hrms.wrapper.OfferDto;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**

This class represents the implementation of the OffersController interface. It is annotated with
the Spring annotations @RestController, @RequestMapping and @Primary to indicate that it is a
Spring-managed bean and should be used as the primary implementation of the OffersController.
It is also annotated with @Api to provide metadata for the Swagger documentation.
The class also uses Lombok's @Slf4j annotation to automatically generate a logger field named "log"
that is used to log method calls and results.
The class fields include an instance of the OffersService bean, which is injected by Spring using
the @Autowired annotation.
The class implements the following methods:
create(Offers Offers): creates an Offers and returns the newly created Offers.
count(String filter): returns the number of Offers that match the specified filter.
search(String filter, Integer offset, Integer size, String orderBy, String orderType): returns
a list of Offers that match the specified filter, sorted according to the specified orderBy
and orderType.
update(Offers Offers): updates an Offers and returns the updated Offers.

importData(MultipartFile excelFile): importing data from excel sheet
export(String filter, Integer offset, Integer size, String orderBy, String orderType): export the data to excel sheet
downloadTemplate(String fileName): download excel sheet template
auditHistory(int id, Integer limit, Integer skip): return AuditHistory of Offers with id and limit and skip
*/
@Primary
@RestController
@RequestMapping("/Offers")
//@Tag(name="/Offers",tags="Offers",description="Offers")
public class OffersControllerImpl implements OffersController {

  private static final Logger log = LogManager.getLogger(OffersControllerImpl.class);

  @Autowired
  private OffersService offersService;
  

	
  @Override
  @TriggerBPMN(entityName = "Offers", appName = "HRMS_APP_NAME")
  @Auditable(actionType = ActionType.CREATE, actionName = "CREATE")
  public Offers create(Offers offers) {
    return offersService.create(offers);
  }

  @Override
  public Long count(String filter) {
    return offersService.count(filter);
  }

  @Override
  public List<Offers> search(String filter, Integer offset, Integer size, String orderBy, String orderType) {
    return offersService.search(filter, offset, size, orderBy, orderType);
  }

  @Override
  @Auditable(actionType = ActionType.UPDATE, actionName = "UPDATE")
  public Offers update(Offers offers) {
    return offersService.update(offers);
  }

  @Override
  public Offers findById(Integer id) {
    return offersService.findById(id);
  }

  @Override
  public List<Offers> findAllById(List<Integer> id) {
    return offersService.findAllById(id);
  }

  @Override
  @Auditable(actionType = ActionType.DELETE, actionName = "DELETE")
  public void deleteById(Integer id) {
    offersService.deleteById(id);
  }
  

	@Override
	public OfferDto getDataForOfferLetter(OfferDto offerDto) {
		log.info("inside the @Class OffersControllerImpl @Method getDataForOfferLetter");
		return offersService.getDataForOfferLetter(offerDto.getApplicantId(), offerDto.getJobOpeningId());
	}

	@Override
	public String createJobOffer(@Valid OfferDto offerDto) {
		return offersService.createJobOffer(offerDto);
	}

	@Override
	public void sendOfferEMail() {
		offersService.sendOfferEMail();
		
	}
  
	
}
