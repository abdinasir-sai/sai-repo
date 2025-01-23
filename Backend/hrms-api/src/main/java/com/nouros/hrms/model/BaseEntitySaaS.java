package com.nouros.hrms.model;

import java.util.Date;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
 
import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.product.security.spring.userdetails.CustomerInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
 
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
 
@MappedSuperclass
@Audited
@FilterDef(name = "CUSTOMER_ID_FILTER", parameters = @ParamDef(name = "customerId", type = Integer.class))
@Filter(name = "CUSTOMER_ID_FILTER", condition = "CUSTOMER_ID = :customerId")
public class BaseEntitySaaS extends BaseEntity{
	
	
	/**
	 * The Customer Id.
	 */
	@Basic
	@Expose
	@JsonIgnore
	@Column(name = "CUSTOMER_ID")
	protected Integer customerId;
	
	
	/**
	 * Gets customer id .
	 *
	 * @return the customer id
	 */
	public  Integer getCustomerId() {
		return customerId;
	}
 
	/**
	 * Sets customer id.
	 *
	 * @param customerId the customer id
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * On create.
	 */
	@PrePersist
	void onCreate() {
		System.out.println("onCreate called  ");
		try {
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			if (customerInfo != null) {
				 customerId = customerInfo.getCustomerWrapper().getId();
				int userid = customerInfo.getUserId();
				System.out.println("when onCreate customerInfo is not null " + customerInfo.getUsername());
				 User userInContext = new User(userid);
				 this.setCreator(userInContext);
				 this.setLastModifier(userInContext);
			}
		} catch (UnsupportedOperationException e) {
			//
		}
		Date date = new Date();
		this.setCreatedTime(date);
		this.setModifiedTime(date);
	}
 
	/**
	 * On update.
	 */
	@PreUpdate
	void onUpdate() {
		System.out.println("onUpdate called  ");
		try {
			CustomerInfo customerInfo = ApplicationContextProvider.getApplicationContext().getBean(CustomerInfo.class);
			if (customerInfo != null) {
				System.out.println("when onUpdate customerInfo is not null " + customerInfo.getUsername());
				customerId = customerInfo.getCustomerWrapper().getId();
				int userid = customerInfo.getUserId();
				User userInContext = new User(userid);
				
				super.setCreator(userInContext);
				super.setLastModifier(userInContext);
				
				;
				System.out.println("customerId  in onUpdate " + customerId);
			}
		} catch (UnsupportedOperationException e) {
			//
		}
		Date date = new Date();
		this.setCreatedTime(date);
		this.setModifiedTime(date);
	}
	
 
}
 
 

