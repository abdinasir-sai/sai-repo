package com.nouros.hrms.service;

import com.nouros.hrms.model.Risk;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * RiskService interface is a service layer interface which handles all the
 * business logic related to Risk model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Visionwaves Risk
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface RiskService extends GenericService<Integer,Risk> {

	public Risk createWithNaming(Risk risk);

}
