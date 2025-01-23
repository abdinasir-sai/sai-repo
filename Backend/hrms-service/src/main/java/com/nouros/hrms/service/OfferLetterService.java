package com.nouros.hrms.service;

import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.wrapper.OfferLetterWrapper;

public interface OfferLetterService {

	
	public String sendHtmlContent(OfferLetterWrapper offerLetterwrapper);
	public String sendOfferLetter(MultipartFile file,OfferLetterWrapper offerLetterWrapper);
}
