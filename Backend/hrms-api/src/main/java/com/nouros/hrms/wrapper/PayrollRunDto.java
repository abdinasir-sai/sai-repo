package com.nouros.hrms.wrapper;

public class PayrollRunDto {
	  private String endDateYear;
	  private String endDateMonth;
	  private Boolean isReRunEnable;
	  private Integer payrollId;

	    public PayrollRunDto(Integer payrollId,String endDateYear,String endDateMonth, boolean isReRunEnable) {
	        this.endDateYear = endDateYear;
	        this.endDateMonth = endDateMonth;
	        this.isReRunEnable = isReRunEnable;
	    	this.payrollId = payrollId;
	    }


		public String getEndDateYear() {
			return endDateYear;
		}


		public void setEndDateYear(String endDateYear) {
			this.endDateYear = endDateYear;
		}


		public String getEndDateMonth() {
			return endDateMonth;
		}


		public void setEndDateMonth(String endDateMonth) {
			this.endDateMonth = endDateMonth;
		}


		public Boolean getIsReRunEnable() {
			return isReRunEnable;
		}


		public void setIsReRunEnable(Boolean isReRunEnable) {
			this.isReRunEnable = isReRunEnable;
		}
		
		public Integer getPayrollId() {
			return payrollId;
		}


		public void setPayrollId(Integer payrollId) {
			this.payrollId = payrollId;
		}
}

