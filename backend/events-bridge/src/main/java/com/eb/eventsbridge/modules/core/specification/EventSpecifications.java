package com.eb.eventsbridge.modules.core.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.eb.eventsbridge.modules.core.entity.Event;
import com.eb.eventsbridge.modules.core.entity.EventStatus;

import jakarta.persistence.criteria.Predicate; // Import this

public class EventSpecifications {

    public static Specification<Event> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            
            String pattern = "%" + keyword.toLowerCase() + "%";
            Predicate title = cb.like(cb.lower(root.get("title")), pattern);
            Predicate desc = cb.like(cb.lower(root.get("description")), pattern);
            Predicate loc = cb.like(cb.lower(root.get("location")), pattern);

            return cb.or(title, desc, loc);
        };
    }

    public static Specification<Event> hasClubId(Long clubId) {
        return (root, query, cb) -> clubId == null ? null : 
            cb.equal(root.get("club").get("id"), clubId);
    }

    public static Specification<Event> filterByDate(LocalDateTime filterStart, LocalDateTime filterEnd) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            
            if (filterStart != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), filterStart));
            }

            
            if (filterEnd != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), filterEnd));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> hasStatus(EventStatus status) {
        return (root, query, cb) -> status == null ? null : 
            cb.equal(root.get("status"), status);
    }
}