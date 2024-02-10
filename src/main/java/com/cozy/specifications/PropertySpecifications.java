package com.cozy.specifications;

import com.cozy.entities.Property;
import com.cozy.enumeration.PropertyStatus;
import com.cozy.enumeration.TunisianCity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecifications {

    public static Specification<Property> locationIsLike(String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("location"), "%" + location + "%");
    }

    public static Specification<Property> roomsBetween(int minRooms, int maxRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("numberOfRooms"), minRooms, maxRooms);
    }

    public static Specification<Property> propertyTypeIs(String propertyType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("propertyType"), propertyType);
    }

    public static Specification<Property> rentPriceBetween(double minRentPrice, double maxRentPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("rentPrice"), minRentPrice, maxRentPrice);
    }

    public static Specification<Property> universityIs(Long universityId) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Property> subRoot = subquery.from(Property.class);
            subquery.select(subRoot.get("id"))
                    .where(criteriaBuilder.equal(subRoot.join("universities").get("id"), universityId));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }

    public static Specification<Property> rentPriceGreaterThanOrEqual(double minRentPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rentPrice"), minRentPrice);
    }

    public static Specification<Property> rentPriceLessThanOrEqual(double maxRentPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("rentPrice"), maxRentPrice);
    }

    public static Specification<Property> roomsGreaterThanOrEqual(int minRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfRooms"), minRooms);
    }

    public static Specification<Property> roomsLessThanOrEqual(int maxRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("numberOfRooms"), maxRooms);
    }

    public static Specification<Property> roomsEqual(int nbRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("numberOfRooms"), nbRooms);
    }
    public static Specification<Property> rentPriceEqual(double rentPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("rentPrice"), rentPrice);
    }
    public static Specification<Property> propertyTypeEqual(String propertyType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("propertyType"), propertyType);
    }
    public static Specification<Property> cityIsLike(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("city"), "%" + city + "%");
    }

    public static Specification<Property> statusIs(PropertyStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

}

