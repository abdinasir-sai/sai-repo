package com.nouros.hrms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Entity
@Data
@Table(name = "EMPLOYEE_SUCCESSOR")
public class EmployeeSuccessor {

	  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	    @Id
	    @Column(name="ID", columnDefinition = "INT")
	    private Integer id;

	    @ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "EMPLOYEE_ID", columnDefinition = "INT")
		private Employee employee;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name ="EMPLOYEE_REVIEW_ID",columnDefinition ="INT")
	    private EmployeeReview employeeReview;
	    
	    @Column(name ="EMPLOYEE_SCORE")
	    private Double employeeScore;
	    
	    @Column(name="FEEDBACK")
	    private String feedBack;
	    
	    @Column(name="READINESS_LEVEL")
	    private String readinessLevel;
	    
	    @ManyToOne
	    @JoinColumn(name="SUCCESSOR_ID",nullable=false,columnDefinition = "INT")
	    private Successor successor;

		public Employee getEmployee() {
			return employee;
		}

		public void setEmployee(Employee employee) {
			this.employee = employee;
		}

		public Successor getSuccessor() {
			return successor;
		}

		public void setSuccessor(Successor successor) {
			this.successor = successor;
		}

}
