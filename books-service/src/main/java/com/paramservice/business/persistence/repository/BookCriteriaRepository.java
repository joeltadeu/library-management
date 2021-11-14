package com.paramservice.business.persistence.repository;

import com.paramservice.business.web.dto.criteria.BookSearchCriteria;
import com.paramservice.business.web.dto.pagination.BookPage;
import com.paramservice.business.persistence.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Repository
public class BookCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public BookCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<BookEntity> findAllWithFilters(BookPage page, BookSearchCriteria criteria){
        CriteriaQuery<BookEntity> criteriaQuery = criteriaBuilder.createQuery(BookEntity.class);
        Root<BookEntity> employeeRoot = criteriaQuery.from(BookEntity.class);
        Predicate predicate = getPredicate(criteria, employeeRoot);
        criteriaQuery.where(predicate);
        setOrder(page, criteriaQuery, employeeRoot);

        TypedQuery<BookEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        typedQuery.setMaxResults(page.getPageSize());

        Pageable pageable = getPageable(page);

        long employeesCount = getEmployeesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private Predicate getPredicate(BookSearchCriteria criteria, Root<BookEntity> employeeRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(criteria.getTitle())){
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        employeeRoot.get("title")
                    ), "%" + criteria.getTitle().toLowerCase(Locale.ROOT) + "%")
            );
        }
        if(Objects.nonNull(criteria.getIsbn())){
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        employeeRoot.get("isbn")
                    ), "%" + criteria.getIsbn().toLowerCase(Locale.ROOT) + "%")
            );
        }
        if(Objects.nonNull(criteria.getAuthor())){
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        employeeRoot.get("author").get("name")
                    ), "%" + criteria.getAuthor().toLowerCase(Locale.ROOT) + "%")
            );
        }
        if(Objects.nonNull(criteria.getCategories())){
            String sql = """ 
                            select bc.book_id
                            from book_category bc
                              join category c
                                on (
                                  bc.category_id = c.id
                                  and c.name in ( :names )
                                ) 
                                """;

            List<Integer> ids = entityManager.createNativeQuery(sql)
                .setParameter("names", criteria.getCategories())
                .getResultList();

            predicates.add( employeeRoot.get("id").in(ids) );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(BookPage page, CriteriaQuery<BookEntity> criteriaQuery, Root<BookEntity> employeeRoot) {
        if(page.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(page.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(page.getSortBy())));
        }
    }

    private Pageable getPageable(BookPage page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(),page.getPageSize(), sort);
    }

    private long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<BookEntity> countRoot = countQuery.from(BookEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
