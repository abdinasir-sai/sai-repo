package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.Nationality;
import com.nouros.hrms.service.generic.GenericService;

public interface NationalityService extends GenericService<Integer,Nationality>{
	
	void softDelete(int id);

	void softBulkDelete(List<Integer> list);

	public Nationality create(Nationality nationality);

}
