package com.nouros.hrms.repository;

import com.nouros.hrms.model.Todo;
import com.nouros.hrms.repository.generic.GenericRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface TodoRepository extends GenericRepository<Todo>{

    
    @Query(value = "SELECT t.* FROM TODO t " +
    "WHERE (" +
    "   (t.ASSIGNOR_UPDATE_FREQUENCY = 'DAILY' AND (t.ASSIGNOR_LAST_NOTIFICATION IS NULL OR t.ASSIGNOR_LAST_NOTIFICATION < CURRENT_DATE)) " +
    "   OR " +
    "   (t.ASSIGNOR_UPDATE_FREQUENCY = 'WEEKLY' AND (t.ASSIGNOR_LAST_NOTIFICATION IS NULL OR t.ASSIGNOR_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -7 DAY))) " +
    "   OR " +
    "   (t.ASSIGNOR_UPDATE_FREQUENCY = 'TWICE' AND (t.ASSIGNOR_LAST_NOTIFICATION IS NULL OR t.ASSIGNOR_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -3 DAY))) " +
    "   OR " +
    "   (t.ASSIGNOR_UPDATE_FREQUENCY = 'ALTERNATIVE' AND (t.ASSIGNOR_LAST_NOTIFICATION IS NULL OR t.ASSIGNOR_LAST_NOTIFICATION <= DATE_ADD(CURRENT_DATE, INTERVAL -2 DAY))) " +
    ") " +
    "AND t.STATUS != 'COMPLETED' AND t.IS_DELETED = 0",
nativeQuery = true)
List<Todo> findTodosForAssignorNotificationToday();

@Query("SELECT t from Todo t where t.referenceId =:referenceId and t.isDeleted = false")
Todo findByReferenceId(@Param("referenceId") String referenceId);

    
    
}
