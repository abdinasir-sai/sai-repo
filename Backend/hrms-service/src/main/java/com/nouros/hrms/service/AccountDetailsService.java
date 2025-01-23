package com.nouros.hrms.service;

import java.util.List;

import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.service.generic.GenericService;

public interface AccountDetailsService extends GenericService<Integer, AccountDetails>{
	
	List<AccountDetails> findAccountDetailsByEmployeeId(Integer id);

}
