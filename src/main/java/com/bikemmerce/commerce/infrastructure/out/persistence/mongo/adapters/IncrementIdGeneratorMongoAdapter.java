package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.adapters;

import com.bikemmerce.commerce.domain.ports.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.CounterDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncrementIdGeneratorMongoAdapter implements IncrementIdGeneratorPort {

    private final MongoOperations mongoOperations;

    @Value("${bikemmerce.database.counters.offset:999}")
    private long offset;

    @Override
    public Long increment(Class<?> clazz) {

        String className = clazz.getSimpleName().toLowerCase();

        Query query = Query.query(Criteria.where("_id").is(className));

        Update update = new Update().inc("counter", 1);

        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        CounterDocument counterDocument = mongoOperations.findAndModify(
                query, update, options, CounterDocument.class);

        return counterDocument != null ? counterDocument.getCounter() + offset : 1000L;
    }
}
