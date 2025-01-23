package com.nouros.hrms.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**

<p>
GenericRepository interface is an extension of the JpaRepository interface.
It is used as a generic repository to perform basic CRUD operations on entities
</p>
@param <T> The type of Entity this repository will handle

*/
@NoRepositoryBean
public interface GenericRepository<T> extends JpaRepository<T,Integer> {

}
