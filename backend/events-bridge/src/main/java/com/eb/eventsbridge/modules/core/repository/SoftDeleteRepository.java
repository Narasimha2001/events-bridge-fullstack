package com.eb.eventsbridge.modules.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SoftDeleteRepository<T, ID> extends JpaRepository<T, ID> {

    @Override
    default void delete(T entity) {
        throw new UnsupportedOperationException("Hard delete is disabled");
    }
}