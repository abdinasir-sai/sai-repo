package com.nouros.hrms.service;

import com.nouros.hrms.model.Jobs;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * JobsService interface is a service layer interface which handles all the
 * business logic related to Jobs model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Jobs
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface JobsService extends GenericService<Integer,Jobs> {

}
