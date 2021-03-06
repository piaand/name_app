package com.example.name_application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * JPA repository interface that does all the name related SQL searches.
 */
public interface NameRepository extends JpaRepository<Name, Long> {
    @Query("SELECT SUM(n.amount) FROM Name n")
    Long findTotalAmount();
    Name findByName(String firstName);
    List<Name> findAllByOrderByAmountDesc();
    List<Name> findAllByOrderByNameAsc();
}