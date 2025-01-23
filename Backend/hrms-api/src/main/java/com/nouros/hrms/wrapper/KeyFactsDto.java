package com.nouros.hrms.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class KeyFactsDto {
	private String college; 
    private List<String> certificates;
    private Double yearsOfExperience; 
}
