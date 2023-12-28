package de.fhg.iese.kickstarttrustee.storage.business.service;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

import reactor.core.publisher.Mono;

@Service
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private static final String COLLECTION = "data_storage_data";
    private static final String _ID = "_id";
    private final ReactiveMongoTemplate mongoTemplate;

    public StorageService(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public Mono<Map<String, Object>> storeData(String id, Map<String, Object> values) {
        final Document document = new Document(values);
        document.put(_ID, new ObjectId(id));
        return mongoTemplate.save(document, COLLECTION)
                .doOnNext(d -> log.info("Stored data: id={}", d.get(_ID)))
                .map(Map::copyOf);
    }

    public Mono<Map<String, Object>> getData(String id) {
        return mongoTemplate.findById(id, BasicDBObject.class, COLLECTION).map(bson -> {
            final Map<String, Object> data = new HashMap<>(bson);
            data.remove(_ID);
            return data;
        });
    }

    public Mono<Void> deleteDataById(String id) {
        return mongoTemplate.remove(new Query(Criteria.where(_ID).is(id)), COLLECTION).then();
    }
}
