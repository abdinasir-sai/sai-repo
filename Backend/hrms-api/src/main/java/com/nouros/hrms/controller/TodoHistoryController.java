package com.nouros.hrms.controller;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.util.APIConstants;
import com.nouros.hrms.wrapper.TodoHistoryData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@ResponseBody
@FeignClient(name = "TodoHistoryController", url = "${hrms.url}", path = "/TodoHistory", primary = false)
public interface TodoHistoryController {

    @Operation(summary = "Creates a new Todo History", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "create")
    TodoHistory create(@RequestBody @Valid TodoHistory todoHistory);

    @Operation(summary = "To get the count of Todo History with RSQL supported filter", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "count")
    Long count(@RequestParam(name = APIConstants.QUERY, required = false) String filter);

     @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@GetMapping(path = "search")
List<TodoHistory> search(@RequestParam(name = APIConstants.QUERY, required = false) String filter,
        @Valid @RequestParam(name = APIConstants.OFFSET, required = true) Integer offset,
        @Valid @RequestParam(name = APIConstants.SIZE, required = true) Integer size,
        @RequestParam(name = APIConstants.ORDER_BY, required = false) String orderBy,
        @RequestParam(name = APIConstants.ORDER_TYPE, required = false) String orderType);

    @Operation(summary = "To update the given Todo History", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @PostMapping(path = "update")
    TodoHistory update(@RequestBody @Valid TodoHistory todoUpdate);

    @Operation(summary = "To delete the given Todo History by Id", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_WRITE" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "deleteById")
    String deleteById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

    @Operation(summary = "To get Todo History by Id", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "findById")
    TodoHistory findById(@Valid @RequestParam(name = APIConstants.ID, required = true) Integer id);

    @Operation(summary = "To get all Todo History by given Ids", security = {
            @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_READ" }) })
    @ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
    @GetMapping(path = "findAllById")
    List<TodoHistory> findAllById(@Valid @RequestParam(name = APIConstants.ID, required = true) List<Integer> id);

    @Operation(summary = "Creates a new Todo History By Todo Id ", security = {
        @SecurityRequirement(name = APIConstants.DEFAULT_SCHEME, scopes = { "ROLE_API_TODO_HISTORY_WRITE" }) })
@ApiResponse(responseCode = APIConstants.SUCCESS_CODE, description = APIConstants.SUCCESS_CODE_MSG)
@PostMapping(path = "createByTodoId")
TodoHistory createByTodoId(@RequestBody @Valid TodoHistoryData todoHistoryData);

}
