package com.nouros.hrms.service.impl;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.repository.AccountDetailsRepository;
import com.nouros.hrms.repository.generic.GenericRepository;
import com.nouros.hrms.service.AccountDetailsService;
import com.nouros.hrms.service.generic.AbstractService;
import com.nouros.hrms.util.report.CommonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl extends AbstractService<Integer,AccountDetails> implements AccountDetailsService{

	public AccountDetailsServiceImpl(GenericRepository<AccountDetails> repository) {
		super(repository, AccountDetails.class);
		
	}

	private static final Logger log = LogManager.getLogger(AccountDetailsServiceImpl.class);
	
	@Autowired
	private AccountDetailsRepository accountDetailsRepository;
	
	@Autowired
	  private CommonUtils commonUtils;

	@Override
	public List<AccountDetails> findAccountDetailsByEmployeeId(Integer id) {
		try {
			log.info("Inside @Class AccountDetailsServiceImpl @Method findAccountDetailsByEmployeeId");
			log.debug("Inside @Class AccountDetailsServiceImpl customerId: {}", commonUtils.getCustomerId());
			List<AccountDetails> result = accountDetailsRepository.findAccountDetailsByEmployeeId(id, commonUtils.getCustomerId());
			log.debug("Inside @Class AccountDetailsServiceImpl @Method findAccountDetailsByEmployeeId result :{}",result);
			return result;
		} catch (Exception e) {
			log.error("Inside @Class AccountDetailsServiceImpl @Method findAccountDetailsByEmployeeId exception occurs :{}",e.getMessage());
			throw new BusinessException("Failed to fetch data from findAccountDetailsByEmployeeId");
		}
	}
	
	

}
