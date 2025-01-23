package com.nouros.hrms.controller;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.Todo;
import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.util.APIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "TodoAssigneeController", url = "${hrms.url}", path = "/TodoAssignee", primary = false)
public interface TodoAssigneeController {

    @Operation(summary = "Creates a new TodoAssignee", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "create")
    TodoAssignee create(@RequestBody TodoAssignee todoAssignee) ;

    @Operation(summary = "To get the count of TodoAssignees with RSQL supported filter", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "count")
    Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

   @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "search")
List<TodoAssignee> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
        @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
        @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
        @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
        @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);


    @Operation(summary = "To update the given TodoAssignee", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "update")
    TodoAssignee update(@RequestBody TodoAssignee todoAssignee);


    @Operation(summary = "To delete the given TodoAssignee by Id", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "deleteById")
    String deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


    @Operation(summary = "To get TodoAssignee by Id", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "findById")
    TodoAssignee findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

    @Operation(summary = "To get all TodoAssignees by given Ids", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_ASSIGNEE_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "findAllById")
    List<TodoAssignee> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> ids);
}
