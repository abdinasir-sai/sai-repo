package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class NDAResponseDto {
	
	String requestParam;
	Integer id;
	String header1;
	String header2;
	List<ClausesDto> clouses;
	String footer1;
	String footer2;
	

}
