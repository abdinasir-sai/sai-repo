package com.nouros.payrollmanagement.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "OTHER_SALARY_COMPONENT_ATTACHMENT")
public class OtherSalaryComponentAttachment {
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
	@Column(columnDefinition = "INT")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OTHER_SALARY_COMPONENT_FK", columnDefinition = "INT")
    private OtherSalaryComponent otherSalaryComponent;
	
	@Size(max = 255)
    @Column(name = "FILE_PATH")
    private String filePath;
}


