package com.nouros.hrms.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.enttribe.core.generic.utils.ApplicationContextProvider;
import com.enttribe.orchestrator.utility.controller.WorkflowActionsController;
import com.enttribe.orchestrator.utility.model.WorkflowActions;

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
import lombok.Data;

@Data
@Entity
@Audited
@Table(name = "HR_BENEFITS")
@Filters(value = { @Filter(name = "hrBenefitsEmployeeIdInFilter",condition = "EMPLOYEE_ID IN (select e.ID FROM EMPLOYEE e WHERE e.REPORTING_MANAGER_USERID_FK = (:id) OR e.USERID_PK = (:id)) "),
@Filter(name = "hrBenefitsEmployeeDepartmentInFilter", condition = "EMPLOYEE_ID IN ( SELECT e.ID FROM EMPLOYEE e where e.DEPARTMENT_ID IN (SELECT d.ID FROM DEPARTMENT d WHERE d.NAME IN (:department)))")})
@FilterDefs(value = { @FilterDef(name = "hrBenefitsEmployeeIdInFilter", parameters = {@ParamDef(name = "id", type = Integer.class)}),
@FilterDef(name = "hrBenefitsEmployeeDepartmentInFilter", parameters = @ParamDef(name = "department", type = String.class))})
public class HrBenefits extends BaseEntitySaaS{

	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	@Id
	@Column(columnDefinition = "INT")
	private Integer id;

	@Column(name = "DELETED")
	private boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
	private Employee employee;

	@Basic
	@Column(name = "BENEFIT_TYPE", columnDefinition = "ENUM('Health Club Benefit','Educational Benefit','Child Education Benefit','New Hire Benefit')")
	private String benefitType;

	@Basic
	@Column(name = "HEALTH_START_DATE", length = 19)
	private Date healthStartDate;

	@Basic
	@Column(name = "HEALTH_END_DATE", length = 19)
	private Date healthEndDate;

	@Basic
	@Column(name = "AMOUNT")
	private Double amount;

	@Basic
	@Column(name = "EDUCATION_START_DATE", length = 19)
	private Date educationStartDate;

	@Basic
	@Column(name = "EDUCATION_END_DATE", length = 19)
	private Date educationEndDate;

	@Basic
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUNTRY_OF_COURSE", columnDefinition = "INT")
	private Country countryOfCourse;

	@Size(max = 255)
	@Basic
	@Column(name = "SAUDI_CITY")
	private String saudiCity;

	@Basic
	@Column(name = "NEW_HIRE_DATE_OF_CLAIM", length = 19)
	private Date newHireDateOfClaim;

	@Basic
	@Column(name = "EMPLOYEE_GRADE", columnDefinition = "INT")
	private Integer employeeGrade;

	@Basic
	@Column(name = "NUMBER_OF_CHILDREN", columnDefinition = "ENUM('1','2','3')")
	private String numberOfChildren;

	@Basic
	@Column(name = "SCHOOL_YEAR", length = 19)
	private Date schoolYear;

	@Size(max = 255)
	@Basic
	@Column(name = "ATTACHMENT")
	private String attachment;

	@Basic
	@Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT")
	private String description;

	@Size(max = 100)
	@Basic
	@Column(name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Size(max = 100)
	@Basic
	@Column(name = "WORKFLOW_STAGE")
	private String workflowStage;

	@Size(max = 250)
	@Column(name = "TEXT", length = 255)
	private String text;

	@Column(name = "OUTPUT_STRING", length = 255)
	private String outputString;
	
	@Basic
	@Column(name = "GL_STATUS", columnDefinition = "ENUM(''PUSHED'','NOT_PUSHED')")
	private String glStatus;
	
	@Basic
    @Column(name = "REMARK", columnDefinition = "LONGTEXT")
    private String remark;
	
	@Column(name="OTHER_EXPENSE_BANK_REQUEST_ID")
	private Integer otherExpenseBankRequestId;
	
	public List<WorkflowActions> getActions() {
        return ApplicationContextProvider.getApplicationContext().getBean(WorkflowActionsController.class).getWorkflowActions(id, "HrBenefits");
    }

}
