package com.nouros.hrms.controller.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nouros.hrms.controller.AccountDetailsController;
import com.nouros.hrms.model.AccountDetails;
import com.nouros.hrms.service.AccountDetailsService;

import jakarta.validation.Valid;

@Primary
@RestController
@RequestMapping("/AccountDetails")
public class AccountDetailsControllerImpl implements AccountDetailsController {
	
	private static final Logger log = LogManager.getLogger(AccountDetailsControllerImpl.class);
	
	@Autowired 
	private AccountDetailsService accountDetailsService;
	
	@Override 
	public AccountDetails create(AccountDetails accountDetails){
		
		return accountDetailsService.create(accountDetails);
	}

	@Override
	public Long count(String filter) {
		
		return accountDetailsService.count(filter);
	}

	@Override
	public List<AccountDetails> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		
		return accountDetailsService.search(filter, offset, size, orderBy, orderType);
	}

	@Override
	public AccountDetails update(@Valid AccountDetails accountDetails) {
	
		return accountDetailsService.update(accountDetails);
	}

	@Override
	public void deleteById(@Valid Integer id) {
		
		 accountDetailsService.deleteById(id);
		
	}

	@Override
	public AccountDetails findById(@Valid Integer id) {
		
		return accountDetailsService.findById(id);
	}

	@Override
	public List<AccountDetails> findAllById(@Valid List<Integer> id) {
		
		return accountDetailsService.findAllById(id);
	}
	
	

	

}
