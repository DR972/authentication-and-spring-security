package com.epam.esm.dao;

import com.epam.esm.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface {@code Dao} describes abstract behavior for working in database.
 *
 * @param <T> indicates that for this instantiation of the DAO, will be used this type of Entity implementation.
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Repository
public interface Dao<T extends BaseEntity<ID>, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> {
}
