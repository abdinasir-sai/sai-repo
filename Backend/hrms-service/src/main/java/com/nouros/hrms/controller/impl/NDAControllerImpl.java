package com.nouros.hrms.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enttribe.core.generic.exceptions.application.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nouros.hrms.controller.NDAController;
import com.nouros.hrms.model.NDA;
import com.nouros.hrms.service.NDAService;
import com.nouros.hrms.wrapper.NDARequestDto;
import com.nouros.hrms.wrapper.NDAResponseDto;

import jakarta.validation.Valid;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/NDA")
public class NDAControllerImpl implements NDAController {


	private static final Logger log = LogManager.getLogger(NDAControllerImpl.class);
	@Autowired
	private NDAService ndaService;

	@Override
	public NDAResponseDto createNDA(NDARequestDto ndaRequestDto) {
		return ndaService.createNDA(ndaRequestDto);
	}

	@Override
	public NDAResponseDto addClouse(NDARequestDto ndaRequestDto) {
		return ndaService.addClouse(ndaRequestDto);
	}

	@Override
	public NDAResponseDto removeClouse(NDARequestDto ndaRequestDto) {
		return ndaService.removeClouse(ndaRequestDto);
	}

	@Override
	public NDAResponseDto findById(@Valid Integer id) {
		return ndaService.findNDAById(id);
	}

	@Override
	public List<NDAResponseDto> search(String filter, @Valid Integer offset, @Valid Integer size, String orderBy,
			String orderType) {
		try {
			List<NDA> listOfNDA = ndaService.search(filter, offset, size, orderBy, orderType);
			List<NDAResponseDto> listOfNdaResponseDto = new ArrayList<>();
			for (NDA nda : listOfNDA) {
				ObjectMapper objectMapper = new ObjectMapper();
			    NDAResponseDto ndaResponseDto = objectMapper.convertValue(nda, NDAResponseDto.class);
			    listOfNdaResponseDto.add(ndaResponseDto);
			}
			return listOfNdaResponseDto;
		} catch (IllegalArgumentException e) {
			log.error("inside @Class NDAControllerImpl @Method search generate exception :{}",e.getMessage());
			throw new BusinessException("error generating inside the search call");
		}
	}

	@Override
	public Long count(String filter) {
		return ndaService.count(filter);
	}

}
