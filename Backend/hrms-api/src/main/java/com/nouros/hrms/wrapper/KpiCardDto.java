package com.nouros.hrms.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KpiCardDto implements Serializable{
	
			private static final long serialVersionUID = 1L;
			private String card;
			private String question;
			private String type;
	
	
}
