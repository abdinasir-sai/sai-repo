package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.RiskCustomField;
import com.nouros.hrms.service.generic.GenericService;

/**

RiskCustomFieldService interface is a service layer interface which handles all the business logic related to RiskCustomField model.

It extends GenericService interface which provides basic CRUD operations.

@author Visionwaves RiskCustomField

@version 1.0

@since 2022-07-01
*/
public interface RiskCustomFieldService extends GenericService<Integer,RiskCustomField> {


   	 	public List<RiskCustomField>  findAllByEntityId(Integer id);
   	 

   
}
