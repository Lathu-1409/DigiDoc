package com.docmanager.repository;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public interface DocumentRepositoryCustom {
    List<DocumentMetadata> updateViewCountForMatchingDocuments(String fileName, String subjectName, DocumentCategory category, String uploadedByUsername);
}