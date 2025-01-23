package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.KpiBuilder;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.KpiCardDto;

/**

KpiBuilderService interface is a service layer interface which handles all the business logic related to KpiBuilder model.

It extends GenericService interface which provides basic CRUD operations.

@author Bootnext KpiBuilder

@version 1.0

@since 2022-07-01
*/
public interface KpiBuilderService extends GenericService<Integer,KpiBuilder> {



	List<KpiCardDto> getKPICardByRole();
	
   
   
}
