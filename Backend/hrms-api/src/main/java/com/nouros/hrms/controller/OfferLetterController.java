package com.nouros.hrms.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.OfferLetterWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@ResponseBody
@FeignClient(name = "OfferLetterController", url = "${hrms.url}", path = "/OfferLetter", primary = false)
public interface OfferLetterController {

	@Operation(summary = "Send Html to Frontend ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_SEND_HTML" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@GetMapping(path = "sendHtmlContent",consumes = MediaType.APPLICATION_JSON_VALUE)
	String sendhtmlContent(@RequestBody OfferLetterWrapper offerLetterWrapper);
	
	@Operation(summary = "Send Offer Letter ", security = {
			@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_OFFER_LETTER" }) })
	@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
	@PostMapping(path = "sendOfferLetter",consumes = MediaType.APPLICATION_JSON_VALUE)
	String sendOfferLetter(@RequestParam("file") MultipartFile file,@RequestBody OfferLetterWrapper offerLetterWrapper);
}
