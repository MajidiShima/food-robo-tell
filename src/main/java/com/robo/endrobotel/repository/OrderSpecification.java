package com.robo.endrobotel.repository;

import com.robo.endrobotel.dto.OrderFilter;
import com.robo.endrobotel.domain.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
public class OrderSpecification{

    public static Specification<Order> filter(OrderFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getTelegramUserId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("user").get("telegramUserId"),
                                filter.getTelegramUserId()
                        )
                );
            }

            if (filter.getFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFrom()
                        )
                );
            }

            if (filter.getTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getTo()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

