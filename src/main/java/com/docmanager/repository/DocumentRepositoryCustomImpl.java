package com.docmanager.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;

import java.util.List;

@Repository
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    public DocumentRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<DocumentMetadata> updateViewCountForMatchingDocuments(String fileName, String subjectName, DocumentCategory category, String uploadedByUsername) {
        Criteria criteria = new Criteria();

        if (fileName != null) {
            criteria.and("fileName").is(fileName);
        }
        if (subjectName != null) {
            criteria.and("subjectName").is(subjectName);
        }
        if (category != null) {
            criteria.and("category").is(category);
        }
        if (uploadedByUsername != null) {
            criteria.and("uploadedByUsername").is(uploadedByUsername);
        }

        Query query = new Query(criteria);
        Update update = new Update().inc("viewCount", 1);

        // Update all matching documents
        mongoTemplate.updateMulti(query, update, DocumentMetadata.class);

        // Retrieve and return the list of updated documents
        return mongoTemplate.find(query, DocumentMetadata.class);
    }
}