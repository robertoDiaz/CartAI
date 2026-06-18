package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.mapper;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.CustomerDocument;

public class CustomerMapper {

    public static CustomerDocument toDocument(Customer customer) {
        return new CustomerDocument(
                customer.getCustomerId().value(), customer.getName(), customer.getEmail());
    }

    public static Customer toDomain(CustomerDocument customerDocument) {
        return new Customer(
                new CustomerId(customerDocument.getId()), customerDocument.getName(),
                customerDocument.getEmail());
    }

}
