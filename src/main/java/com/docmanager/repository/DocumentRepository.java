package com.docmanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;

import java.util.List;

public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {

    List<DocumentMetadata> findByFileNameAndSubjectNameAndCategoryAndUploadedByUsername(
            String fileName, String subjectName, DocumentCategory category, String uploadedByUsername);

    List<DocumentMetadata> findByFileNameAndSubjectName(String fileName, String subjectName);

    List<DocumentMetadata> findByCategory(DocumentCategory category);

    List<DocumentMetadata> findByUploadedByUsername(String uploadedByUsername);
}