package com.nouros.hrms.model;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TO_DO_BUCKET")
public class TodoBucket extends BaseEntitySaaS{
	
	
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
	
	@Size(max = 100)
    @Basic
    @Column(name = "NAME")
    private String name;
	
	
	@Basic
    @Column(name = "DEFAULT_BUCKET")
    private boolean defaultBucket;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefaultBucket() {
		return defaultBucket;
	}

	public void setDefaultBucket(boolean defaultBucket) {
		this.defaultBucket = defaultBucket;
	}
	
	

}
