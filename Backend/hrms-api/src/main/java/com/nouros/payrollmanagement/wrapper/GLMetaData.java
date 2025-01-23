package com.nouros.payrollmanagement.wrapper;

 
import java.util.List; 
 
import lombok.Data;

@Data
public class GLMetaData   {
	
    private String ledger_id;
	
	private String batch_name;
	
	private String batch_description;
	
    private String accounting_date;
	
	private String user_source_name;
	
	private String user_category_name;
	
	private String error_to_suspense_indicator;
	
	private String summary_indicator;
	

	private String import_descriptive_flex;
	
	private List<GLEntries> gl_entries;
	

	public List<GLEntries> getGl_entries() {
		return gl_entries;
	}

	public void setGl_entries(List<GLEntries> gl_entries) {
		this.gl_entries = gl_entries;
	}

		public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}

	public String getBatch_description() {
		return batch_description;
	}

	public void setBatch_description(String batch_description) {
		this.batch_description = batch_description;
	}

	public String getAccounting_date() {
		return accounting_date;
	}

	public void setAccounting_date(String accounting_date) {
		this.accounting_date = accounting_date;
	}

	public String getUser_source_name() {
		return user_source_name;
	}

	public void setUser_source_name(String user_source_name) {
		this.user_source_name = user_source_name;
	}

	public String getUser_category_name() {
		return user_category_name;
	}

	public void setUser_category_name(String user_category_name) {
		this.user_category_name = user_category_name;
	}

	public String getImport_descriptive_flex() {
		return import_descriptive_flex;
	}

	public void setImport_descriptive_flex(String import_descriptive_flex) {
		this.import_descriptive_flex = import_descriptive_flex;
	}

	public String getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(String ledger_id) {
		this.ledger_id = ledger_id;
	}

	public String getError_to_suspense_indicator() {
		return error_to_suspense_indicator;
	}

	public void setError_to_suspense_indicator(String error_to_suspense_indicator) {
		this.error_to_suspense_indicator = error_to_suspense_indicator;
	}

	public String getSummary_indicator() {
		return summary_indicator;
	}

	public void setSummary_indicator(String summary_indicator) {
		this.summary_indicator = summary_indicator;
	}
	
	
	
}
