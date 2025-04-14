package com.docmanager.repository;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Extend MongoRepository and Custom Repository Interface
@Repository
public interface DocumentRepository extends MongoRepository<DocumentMetadata, String>, DocumentRepositoryCustom {

    // Default methods for querying documents based on parameters
    List<DocumentMetadata> findByFileNameAndSubjectNameAndCategoryAndUploadedByUsername(
            String fileName, String subjectName, DocumentCategory category, String uploadedByUsername);

    List<DocumentMetadata> findByFileNameAndSubjectName(String fileName, String subjectName);

    List<DocumentMetadata> findByCategory(DocumentCategory category);

    List<DocumentMetadata> findByUploadedByUsername(String uploadedByUsername);

    List<DocumentMetadata> findByFileName(String fileName);
}