package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Competencies;
import com.nouros.hrms.service.generic.GenericService;

/**
 * 
 * CompetenciesService interface is a service layer interface which handles all
 * the business logic related to Competencies model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Competencies
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface CompetenciesService extends GenericService<Integer, Competencies> {

	List<Competencies> getCompetenciesList();

}
