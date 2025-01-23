package com.nouros.payrollmanagement.wrapper;

import java.util.Date;

import lombok.Data;

@Data
public class GLEntries {

	private String ledger_id;

	private String period_name;

	private String je_accounting_date;

	private String user_je_source_name;

	private String user_je_category_name;

	private String group_id;

	private String balance_type;

	private String company_code;

	private String location_code;

	private String lob_code;

	private String cc_code;

	private String project_code;

	private String natural_acc_code;

	private String intercomp_code;

	private String custom_code1;

	private String custom_code2;

	private String currency_code;

	private String entered_dr_amt;

	private String accounted_dr;

	private String entered_cr_amt;

	private String accounted_cr;

	private String batch_name;

	private String je_name;

	private String je_desc;

	private String version_number;

	private String status;

	private String currency_date;

	private String curr_conver_rate;

	private String user_curr_conver_type;

	public String getPeriod_name() {
		return period_name;
	}

	public void setPeriod_name(String period_name) {
		this.period_name = period_name;
	}

	public String getJe_accounting_date() {
		return je_accounting_date;
	}

	public void setJe_accounting_date(String je_accounting_date) {
		this.je_accounting_date = je_accounting_date;
	}

	public String getUser_je_source_name() {
		return user_je_source_name;
	}

	public void setUser_je_source_name(String user_je_source_name) {
		this.user_je_source_name = user_je_source_name;
	}

	public String getUser_je_category_name() {
		return user_je_category_name;
	}

	public void setUser_je_category_name(String user_je_category_name) {
		this.user_je_category_name = user_je_category_name;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getBalance_type() {
		return balance_type;
	}

	public void setBalance_type(String balance_type) {
		this.balance_type = balance_type;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}

	public String getLob_code() {
		return lob_code;
	}

	public void setLob_code(String lob_code) {
		this.lob_code = lob_code;
	}

	public String getCc_code() {
		return cc_code;
	}

	public void setCc_code(String cc_code) {
		this.cc_code = cc_code;
	}

	public String getProject_code() {
		return project_code;
	}

	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}

	public String getNatural_acc_code() {
		return natural_acc_code;
	}

	public void setNatural_acc_code(String natural_acc_code) {
		this.natural_acc_code = natural_acc_code;
	}

	public String getIntercomp_code() {
		return intercomp_code;
	}

	public void setIntercomp_code(String intercomp_code) {
		this.intercomp_code = intercomp_code;
	}

	public String getCustom_code1() {
		return custom_code1;
	}

	public void setCustom_code1(String custom_code1) {
		this.custom_code1 = custom_code1;
	}

	public String getCustom_code2() {
		return custom_code2;
	}

	public void setCustom_code2(String custom_code2) {
		this.custom_code2 = custom_code2;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}

	public String getJe_name() {
		return je_name;
	}

	public void setJe_name(String je_name) {
		this.je_name = je_name;
	}

	public String getJe_desc() {
		return je_desc;
	}

	public void setJe_desc(String je_desc) {
		this.je_desc = je_desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency_date() {
		return currency_date;
	}

	public void setCurrency_date(String currency_date) {
		this.currency_date = currency_date;
	}

	public String getUser_curr_conver_type() {
		return user_curr_conver_type;
	}

	public void setUser_curr_conver_type(String user_curr_conver_type) {
		this.user_curr_conver_type = user_curr_conver_type;
	}

	public String getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(String ledger_id) {
		this.ledger_id = ledger_id;
	}

	public String getVersion_number() {
		return version_number;
	}

	public void setVersion_number(String version_number) {
		this.version_number = version_number;
	}

	public String getCurr_conver_rate() {
		return curr_conver_rate;
	}

	public void setCurr_conver_rate(String curr_conver_rate) {
		this.curr_conver_rate = curr_conver_rate;
	}

	public String getEntered_dr_amt() {
		return entered_dr_amt;
	}

	public void setEntered_dr_amt(String entered_dr_amt) {
		this.entered_dr_amt = entered_dr_amt;
	}

	public String getAccounted_dr() {
		return accounted_dr;
	}

	public void setAccounted_dr(String accounted_dr) {
		this.accounted_dr = accounted_dr;
	}

	public String getEntered_cr_amt() {
		return entered_cr_amt;
	}

	public void setEntered_cr_amt(String entered_cr_amt) {
		this.entered_cr_amt = entered_cr_amt;
	}

	public String getAccounted_cr() {
		return accounted_cr;
	}

	public void setAccounted_cr(String accounted_cr) {
		this.accounted_cr = accounted_cr;
	}
	
	
	

}
