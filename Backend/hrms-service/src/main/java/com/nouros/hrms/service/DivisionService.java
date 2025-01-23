package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Division;
import com.nouros.hrms.service.generic.GenericService;

public interface DivisionService extends GenericService<Integer, Division> {

	List<Division> findAll();

}
