package com.orderservice.business.persistence.repository;

import com.orderservice.business.persistence.entity.BookOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrderEntity, Long> {

    @Query("select new java.lang.Boolean(count(*) > 0) from BookOrderEntity where bookId = :bookId and returnedAt is null")
    boolean hasBookLoaned(Long bookId);

    @Query(value = "from BookOrderEntity where bookId = :bookId and userId = :userId and returnedAt is null")
    Optional<BookOrderEntity> findOpenLoanedByUserIdAndBookId(Long userId, Long bookId);

    @Query(value = "select count(*) from BookOrderEntity where userId = :userId and returnedAt is null")
    Integer countOpenLoanedByUserId(Long userId);


    @Query("select new java.lang.Boolean(count(*) > 0) from BookOrderEntity where userId = :userId and returnedAt is " +
           "null and returnIn < current_timestamp")
    boolean hasOutstandingBookLoaned(Long userId);
}
