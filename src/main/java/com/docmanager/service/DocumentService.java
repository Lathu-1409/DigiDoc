package com.docmanager.service;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;
import com.docmanager.model.DocumentMetadata.Sem;
import com.docmanager.repository.DocumentRepository;
import com.docmanager.repository.DocumentRepositoryCustom;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.mongodb.core.query.Update;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    @Qualifier("documentRepositoryCustomImpl")
    private DocumentRepositoryCustom documentRepositoryCustom;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Uploads a document to MongoDB.
     * 
     * Validates that the file is a PDF and ensures that its size is less than 14 MB. 
     * Converts relevant metadata fields to lowercase for consistent storage.
     * 
     * @param fileName          Name of the file being uploaded.
     * @param subjectName       Subject associated with the file.
     * @param category          Category of the document (Enum).
     * @param file              PDF file to be uploaded.
     * @param uploadedByUsername Username of the uploader.
     * @return Saved DocumentMetadata object.
     * @throws IOException      If file upload fails.
     * @throws IllegalArgumentException If file is not a PDF or exceeds 14 MB.
     */
    public DocumentMetadata uploadDocument(String fileName, String subjectName, DocumentCategory category,Sem sem, MultipartFile file, String uploadedByUsername) throws IOException {
        // Validate that the file is a PDF
        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Validate file size (less than 14 MB)
        long maxSizeInBytes = 14 * 1024 * 1024; // 14 MB in bytes
        if (file.getSize() > maxSizeInBytes) {
            throw new IllegalArgumentException("File size must be less than 14 MB.");
        }

        // Convert fields to lowercase before saving
        String lowerCaseFileName = fileName != null ? fileName.toLowerCase() : null;
        String lowerCaseSubjectName = subjectName != null ? subjectName.toLowerCase() : null;
        String lowerCaseUploadedByUsername = uploadedByUsername != null ? uploadedByUsername.toLowerCase() : null;

        // Create and save the document metadata
        DocumentMetadata document = new DocumentMetadata();
        document.setFileName(lowerCaseFileName);
        document.setSubjectName(lowerCaseSubjectName);
        document.setCategory(category); // Enum values are case-insensitive, no need to convert
        document.setUploadedByUsername(lowerCaseUploadedByUsername);
        document.setPdfFile(new Binary(file.getBytes())); // Save PDF content as binary
        document.setDownloadCount(0);
        document.setViewCount(0);
        document.setSem(sem);
        return documentRepository.save(document); // Save to MongoDB
    }

    /**
     * Queries documents based on the provided filters.
     * 
     * Converts filter parameters to lowercase before querying for case-insensitivity. 
     * Updates the view count of matching documents using custom repository logic.
     * 
     * @param fileName          (Optional) The file name to search.
     * @param subjectName       (Optional) The subject name to search.
     * @param category          (Optional) The category to search.
     * @param uploadedByUsername (Optional) The username to search.
     * @return List of matching documents with updated view counts.
     */
    public List<DocumentMetadata> queryDocuments(Optional<String> fileName, Optional<String> subjectName, Optional<DocumentCategory> category, Optional<String> uploadedByUsername) {
        // Extract values and convert them to lowercase if present
        String fileNameValue = fileName.filter(f -> !f.isBlank()).map(String::toLowerCase).orElse(null);
        String subjectNameValue = subjectName.filter(s -> !s.isBlank()).map(String::toLowerCase).orElse(null);
        DocumentCategory categoryValue = category.orElse(null);
        String uploadedByUsernameValue = uploadedByUsername.filter(u -> !u.isBlank()).map(String::toLowerCase).orElse(null);

        // Use the custom repository method to update viewCount and retrieve matching documents
        return documentRepositoryCustom.updateViewCountForMatchingDocuments(
            fileNameValue,
            subjectNameValue,
            categoryValue,
            uploadedByUsernameValue
        );
    }

    /**
     * Downloads a document by its ID.
     * 
     * Increments the download count for the document before returning its binary content.
     * 
     * @param documentId The ID of the document to fetch.
     * @return Binary content of the document.
     * @throws IllegalArgumentException If the document is not found.
     */
    public byte[] downloadDocumentById(String documentId) {
        DocumentMetadata document = incrementDownloadCount(documentId)
            .orElseThrow(() -> new IllegalArgumentException("Document not found for the provided ID: " + documentId));

        return document.getPdfFile().getData();
    }

    /**
     * Increments the download count for a document by its ID.
     * 
     * Uses MongoTemplate to update the download count atomically.
     * 
     * @param documentId The ID of the document to update.
     * @return Updated document wrapped in an Optional, or Optional.empty() if not found.
     */
    public Optional<DocumentMetadata> incrementDownloadCount(String documentId) {
        Criteria criteria = Criteria.where("id").is(documentId);
        Update update = new Update().inc("downloadCount", 1);

        DocumentMetadata updatedDocument = mongoTemplate.findAndModify(
            new Query(criteria),
            update,
            DocumentMetadata.class
        );

        return Optional.ofNullable(updatedDocument); // Wrap the result in an Optional
    }
    public void deleteDocumentById(String documentId) {
        documentRepository.deleteById(documentId);
    }

    public List<DocumentMetadata> getRecentlyUploadedDocuments(int n) {
        return documentRepository.findAll().stream()
            .sorted(Comparator.comparing(DocumentMetadata::getId).reversed()) // Assuming IDs are sequential
            .limit(n)
            .toList();
    }

    public List<DocumentMetadata> getTopMostViewedDocuments(int n) {
        return documentRepository.findAll().stream()
            .sorted(Comparator.comparingLong(DocumentMetadata::getViewCount).reversed())
            .limit(n)
            .toList();
    }

    public List<DocumentMetadata> getTopMostDownloadedDocuments(int n) {
        return documentRepository.findAll().stream()
            .sorted(Comparator.comparingLong(DocumentMetadata::getDownloadCount).reversed())
            .limit(n)
            .toList();
    }
    public List<DocumentMetadata> getDocumentsBySemester(Sem sem, int n) {
        return documentRepository.findAll().stream()
            .filter(document -> document.getSem() == sem) // Filter by semester
            .limit(n) // Limit results to `n`
            .toList();
    }
}