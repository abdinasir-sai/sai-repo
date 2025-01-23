package com.nouros.hrms.controller;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.Todo;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.TodoDto;
import com.nouros.hrms.wrapper.TodoUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "TodoController", url = "${hrms.url}", path = "/Todo", primary = false)
public interface TodoController {

     @Operation(summary = "Creates a new Todo", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO__WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path = "create")
Todo create(@RequestBody Todo Todo);

@Operation(summary = "To get the count with RSQL supported filter", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_READ" }) })

@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "count")
Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

@Operation(summary = "To get the list of Todo with RSQL supported filter", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_READ" }) })

@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "search")
List<Todo> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
        @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
        @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
        @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
        @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType,
        @RequestParam(name = "assignee", required = false) Integer assignee);

@Operation(summary = "To update the given Todo", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })

@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path = "update")
Todo update(@RequestBody Todo Todo);

@Operation(summary = "To delete the given Todo by Id", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "deleteById")
String deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);


@Operation(summary = "To get Todo by Id", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_READ" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "findById")
Todo findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

@Operation(summary = "To get all Todo by given Ids", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_READ" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "findAllById")
List<Todo> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

@Operation(summary = "Manually trigger scheduled notifications", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "/triggerScheduledNotifications")
    ResponseEntity<String> triggerScheduledNotifications();

@Operation(summary = "create Todo And Todo Assignee", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path = "createTodoAndAssignee" , consumes = MediaType.APPLICATION_JSON_VALUE)
Object createTodoAndAssignee(@RequestBody List<TodoDto> todoDtos);

@Operation(summary = "update Todo Status By Id", security = {
@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping("updateTodoStatus")
String updateTodoStatus(@RequestParam(name = "id",required = true)Integer id,@RequestParam(name = "status",required = true)String status);

@Operation(summary = "update assignee", security = {
@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping("updateAssignee")
String updateAssignee(@RequestParam(name = "id", required = true) Integer id,
                @RequestParam(name = "oldAssigneeId", required = true) Integer oldAssigneeId,
                @RequestParam(name = "newAssigneeId", required = true) Integer newAssigneeId);

@Operation(summary = "Update Deadline", security = {
    @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping("updateDeadline")
String updateDeadline(@RequestParam(name = "id", required = true) Integer id,
                              @RequestParam(name = "newDueDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date newDueDate);
                              
@Operation(summary = "find All Data By Todo Id", security = {
@SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping("findAllData")
Object findAllData(@RequestParam(name = "referenceId", required = true) String referenceId,
                @RequestParam(name = "assigneeId", required = true) Integer assigneeId);

@Operation(summary = "Update Assignees and Deadline", security = {
    @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping("updateTodo")
String updateTodo(@RequestBody TodoUpdateRequest todoUpdateRequest);

}
