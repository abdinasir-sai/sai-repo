package com.nouros.hrms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "INTEGRATION_LOG")
public class IntegrationLog {

    public static final String CONNECTOR_ID = "connectorId";
    public static final String STATUS = "status";
    public static final String APPLICATION = "APPLICATION";
    public static final String CONNECTOR_NAME = "connectorName";
    public static final String HEADERS = "headers";
    public static final String INTEGRATION_TYPE = "integrationType";
    public static final String REQUEST_TYPE = "requestType";
    public static final String CAN_NOT_BE_NULL = " can not be null";
    public static final String SIZE_SHOULD_BE_LESS_THAN = " size should be less than ";
    public static final String URL = "url";
    public static final String RETRY_COUNT = "retryCount";
    public static final String PROCESSDEFINITION_ID = "processDefinitionId";
    public static final String PROCESS_INSTANCE_ID = "processInstanceId";
    
	public enum Status {
		
	    WAITING, FAILED, COMPLETED
	  }

	  public enum RequestType {
	    IN_BOUND, OUT_BOUND
	  }

	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Id
	  @Column(name = "ID", columnDefinition = "INT")
	  private Integer id;

	  @Basic
	  @NotNull(message = URL + CAN_NOT_BE_NULL)
	  @Size(min = 1, max = 500, message = URL + SIZE_SHOULD_BE_LESS_THAN + 500)
	  @Column(name = "URL", length = 500, nullable = false)
	  private String url;

	  @Size(max = 4000, message = HEADERS + SIZE_SHOULD_BE_LESS_THAN + 4000)
	  @Column(name = "HEADER", length = 4000)
	  private String headers;

	  @Column(name = "PAYLOAD", columnDefinition = "TEXT")
	  private String payload;

	   @Size(min = 1, max = 100, message = INTEGRATION_TYPE + SIZE_SHOULD_BE_LESS_THAN + 100)
	  @Column(name = "INTEGRATION_TYPE", length = 100, nullable = false)
	  private String integrationType;

	  @NotNull(message = RETRY_COUNT + CAN_NOT_BE_NULL)
	  @Column(name = "RETRY_COUNT", length = 5, nullable = false)
	  private Integer retryCount;

	  @Column(name = "RESPONSE", length = 4000)
	  @Size(max = 4000, message = "RESPONSE" + SIZE_SHOULD_BE_LESS_THAN + 4000)
	  private String response;

	  @Enumerated(EnumType.STRING)
	   
	  @Column(name = "STATUS", length = 100, nullable = false)
	  private Status status;

	  @Column(name = "CREATED_TIME", insertable = true, updatable = false)
	  private Date createdTime;

	  @NotNull(message = REQUEST_TYPE + CAN_NOT_BE_NULL)
	  @Size(min = 1, max = 100, message = REQUEST_TYPE + SIZE_SHOULD_BE_LESS_THAN + 100)
	  @Column(name = "HTTP_TYPE", length = 100, nullable = false)
	  private String httpType;

	  
	  
	  @Column(name="BATCH_NAME")
	  private Integer batchName;


      @Column(name = "PROCESS_ID")
      private Double processId;
      
      @Column(name="FINAL_STATUS")
       private String finalStatus;

      @Column(name = "REQUEST_ID")
      private String requestId;

      @Column(name = "PAYROLL_ID")
      private Integer payrollId;

 
      
}
