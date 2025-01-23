package com.nouros.hrms.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.controller.OfferLetterController;
import com.nouros.hrms.service.OfferLetterService;
import com.nouros.hrms.wrapper.OfferLetterWrapper;

@RestController
@RequestMapping("/OfferLetter")
public class OfferLetterControllerImpl implements  OfferLetterController{

	@Autowired
	OfferLetterService offerLetterService;
	
	@Override
	public String sendhtmlContent(OfferLetterWrapper offerLetterWrapper) {
	 
		return offerLetterService.sendHtmlContent(offerLetterWrapper);
	}
	
	@Override
	public String sendOfferLetter(MultipartFile file,OfferLetterWrapper offerLetterWrapper) {
	 
		return offerLetterService.sendOfferLetter(file,offerLetterWrapper);
	}

}
