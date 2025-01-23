package com.nouros.hrms.service;

import com.nouros.hrms.model.Offers;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.OfferDto;

/**
 * 
 * OffersService interface is a service layer interface which handles all the
 * business logic related to Offers model.
 * 
 * It extends GenericService interface which provides basic CRUD operations.
 * 
 * @author Bootnext Offers
 * 
 * @version 1.0
 * 
 * @since 2022-07-01
 */
public interface OffersService extends GenericService<Integer,Offers> {

	Offers findByProcessInstanceId(String processInstanceId);

	OfferDto getDataForOfferLetter(int applicantId, int jobOpeningId);

	String createJobOffer(OfferDto offerDto);

	void sendOfferEMail(Offers offerRecived);

	void sendOfferEMail();

}
