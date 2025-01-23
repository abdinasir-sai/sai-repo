package com.nouros.hrms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.NDARequestDto;
import com.nouros.hrms.wrapper.NDAResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;


public interface NDAController {

	@PostMapping("/createNDA")
	@Operation(summary = "Create NDA", tags = "NDA", description = ".")
	NDAResponseDto createNDA(@RequestBody NDARequestDto ndaRequestDto);

	@PostMapping("/addClouse")
	@Operation(summary = "Add  Clouse", tags = "NDA", description = ".")
	NDAResponseDto addClouse(@RequestBody NDARequestDto ndaRequestDto);

	@PostMapping("/removeClouse")
	@Operation(summary = "Remove Clouse", tags = "NDA", description = ".")
	NDAResponseDto removeClouse(@RequestBody NDARequestDto ndaRequestDto);

	@Operation(summary = "To get NDAResponseDto by Id", tags = "NDA", description = ".")
	@GetMapping("/findById")
	NDAResponseDto findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

	@Operation(summary = "To get the list of NDAResponseDto with RSQL supported filter", tags = "NDA", description = ".")
	@GetMapping("/search")
	List<NDAResponseDto> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
			@Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
			@Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
			@RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
			@RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

	@Operation(summary = "to get the count ", tags = "NDA", description = ".")
	@GetMapping("/count")
	Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

}
