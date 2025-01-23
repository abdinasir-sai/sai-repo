package com.nouros.payrollmanagement.service;



import com.nouros.hrms.service.generic.GenericService;
import com.nouros.payrollmanagement.model.MasterData;

public interface MasterDataService extends GenericService<Integer, MasterData> {
	
	public MasterData create(MasterData masterData);

}
