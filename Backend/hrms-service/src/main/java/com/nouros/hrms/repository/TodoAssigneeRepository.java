package com.nouros.hrms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nouros.hrms.model.TodoAssignee;
import com.nouros.hrms.repository.generic.GenericRepository;

import org.springframework.data.repository.query.Param;


@Repository
@Transactional(readOnly = true)
public interface TodoAssigneeRepository extends GenericRepository<TodoAssignee>{



    @Query(value = "SELECT ta.* FROM TODO_ASSIGNEE ta " +
    "JOIN TODO t ON ta.TODO_ID = t.ID " +
    "WHERE (" +
    "   (ta.ASSIGNEE_NOTIFICATION_FREQUENCY = 'DAILY' AND (ta.ASSIGNEE_LAST_NOTIFICATION IS NULL OR ta.ASSIGNEE_LAST_NOTIFICATION < CURRENT_DATE)) " +
    "   OR " +
    "   (ta.ASSIGNEE_NOTIFICATION_FREQUENCY = 'WEEKLY' AND (ta.ASSIGNEE_LAST_NOTIFICATION IS NULL OR ta.ASSIGNEE_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY))) " +
    "   OR " +
    "   (ta.ASSIGNEE_NOTIFICATION_FREQUENCY = 'TWICE' AND (ta.ASSIGNEE_LAST_NOTIFICATION IS NULL OR ta.ASSIGNEE_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY))) " +
    "   OR " +
    "   (ta.ASSIGNEE_NOTIFICATION_FREQUENCY = 'ALTERNATIVE' AND (ta.ASSIGNEE_LAST_NOTIFICATION IS NULL OR ta.ASSIGNEE_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY))) " +
    ") " +
    "AND t.STATUS != 'COMPLETED' " +
    "AND ta.IS_DELETED = 0",
nativeQuery = true)
List<TodoAssignee> findAssigneesForNotificationToday();



@Query("SELECT ta FROM TodoAssignee ta WHERE ta.todo.id = :todoId AND ta.isDeleted = false")
List<TodoAssignee> findAssigneesByTodoId(@Param("todoId") Integer todoId);

@Query("SELECT ta FROM TodoAssignee ta WHERE ta.assignee.userid = :userId AND ta.todo.id = :todoId AND ta.isDeleted = false")
TodoAssignee findTodoAssigneeByUserId(@Param("userId") Integer userId,@Param("todoId") Integer todoId);

}
