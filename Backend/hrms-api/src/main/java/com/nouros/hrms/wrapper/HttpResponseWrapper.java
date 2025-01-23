package com.nouros.hrms.wrapper;

import lombok.Data;

@Data
public class HttpResponseWrapper {
 
  private Integer statusCode;
  private String response;
 
  public Integer getStatusCode() {
    return statusCode;
  }
 
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }
 
  public String getResponse() {
    return response;
  }
 
  public void setResponse(String response) {
    this.response = response;
  }
 
}