package com.nouros.hrms.controller;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "IntegrationLogController", url = "${workflow-service.url}", path = "IntegrationLog", primary = false)
public interface IntegrationLogController {

 
 
}
