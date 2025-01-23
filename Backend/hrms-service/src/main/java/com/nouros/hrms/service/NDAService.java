package com.nouros.hrms.service;

import com.nouros.hrms.model.NDA;
import com.nouros.hrms.service.generic.GenericService;
import com.nouros.hrms.wrapper.NDARequestDto;
import com.nouros.hrms.wrapper.NDAResponseDto;


public interface NDAService extends GenericService<Integer,NDA>{

	NDAResponseDto createNDA(NDARequestDto ndaRequestDto);

	NDAResponseDto addClouse(NDARequestDto ndaRequestDto);

	NDAResponseDto removeClouse(NDARequestDto ndaRequestDto);
	
	NDAResponseDto findNDAById( Integer id);

}
