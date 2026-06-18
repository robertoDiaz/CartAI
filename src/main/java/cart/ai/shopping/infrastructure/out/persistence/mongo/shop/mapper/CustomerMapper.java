package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mapper;

import cart.ai.shopping.domain.model.security.value.objects.Email;
import cart.ai.shopping.domain.model.security.value.objects.UserId;
import cart.ai.shopping.domain.model.shop.Customer;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.CustomerDocument;

public class CustomerMapper {

    public static CustomerDocument toDocument(Customer customer) {
        return CustomerDocument.builder()
                .id(customer.userId().value())
                .name(customer.name())
                .email(customer.email().value())
                .build();
    }

    public static Customer toDomain(CustomerDocument customerDocument) {
        return new Customer(
                new UserId(customerDocument.getId()),
                customerDocument.getName(),
                new Email(customerDocument.getEmail())
        );
    }
}
