package com.nouros.payrollmanagement.model;


import com.nouros.hrms.model.BaseEntitySaaS;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "MASTER_DATA")
public class MasterData extends BaseEntitySaaS{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;
	
	@Basic
	@Column(name ="NAME")
	private String name;
	
	@Basic
	@Column(name="TYPE")
	private String type;
	
	@Basic
	@Column(name="CODE")
	private Integer code;
	
	@Basic 
	@Column(name="SUB_TYPE")
	private String subType;	
}
