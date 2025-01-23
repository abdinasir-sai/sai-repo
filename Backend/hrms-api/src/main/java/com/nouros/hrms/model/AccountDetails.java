package com.nouros.hrms.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ACCOUNT_DETAILS")
public class AccountDetails extends BaseEntitySaaS{
	
	 @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	 @Id
	 @Column(columnDefinition = "INT")
	 private Integer id;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @Basic
	 @JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	 private Employee employee;
	 
	 @Size(max = 100)
	 @Basic
	 @Column(name = "IBAN",length = 100)
	 private String iban;
	 
	 @Size(max = 100)
	 @Basic
	 @Column(name = "BENEFICIARY_NAME",length = 100)
	 private String beneficiaryName;
	 
	 @Size(max = 100)
	 @Basic
	 @Column(name = "BENEFICIARY_ID",length = 100)
	 private String beneficiaryId;
	 
	 @Size(max = 100)
	 @Basic
	 @Column(name = "BANK_ID",length = 100)
	 private String bankId;
	 
	 @Size(max = 200)
	 @Basic
	 @Column(name = "PAYMENT_NARRATION",length = 200)
	 private String paymentNarration;
	 
	 @Size(max = 200)
	 @Basic
	 @Column(name = "BANK_DOCUMENT",length = 200)
	 private String bankDocument;
	 
	 @Size(max = 200)
	 @Basic
	 @Column(name = "IBAN_CERTIFICATE",length = 200)
	 private String ibanCertificate;

	public String getBankDocument() {
		return bankDocument;
	}

	public void setBankDocument(String bankDocument) {
		this.bankDocument = bankDocument;
	}

	public String getIbanCertificate() {
		return ibanCertificate;
	}

	public void setIbanCertificate(String ibanCertificate) {
		this.ibanCertificate = ibanCertificate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPaymentNarration() {
		return paymentNarration;
	}

	public void setPaymentNarration(String paymentNarration) {
		this.paymentNarration = paymentNarration;
	}
	 
}
