package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TodoHistory;
import com.nouros.hrms.repository.generic.GenericRepository;


@Repository
@Transactional(readOnly = true)
public interface TodoHistoryRepository extends GenericRepository<TodoHistory>{

@Query("SELECT th FROM TodoHistory th WHERE  th.todo.id = :todoId AND th.user.userid = :assigneeId AND th.isDeleted = false")
List<TodoHistory>  findTodoHistoryByTodoIdAndTodoAssignee(@Param("todoId") Integer todoId,@Param("assigneeId") Integer assigneeId);

}
