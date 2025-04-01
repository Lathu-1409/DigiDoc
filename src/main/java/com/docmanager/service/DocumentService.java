package com.docmanager.service;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;
import com.docmanager.repository.DocumentRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    /**
     * Accepts only PDF files and saves to MongoDB.
     * 
     * @param fileName          Name of the file being uploaded.
     * @param subjectName       Subject associated with the file.
     * @param category          Category of the document (Enum).
     * @param file              PDF file to be uploaded.
     * @param uploadedByUsername Username of the uploader.
     * @return Saved DocumentMetadata object.
     * @throws IOException      If file upload fails.
     * @throws IllegalArgumentException If file is not a PDF.
     */
    public DocumentMetadata uploadDocument(String fileName, String subjectName, DocumentCategory category, MultipartFile file, String uploadedByUsername) throws IOException {
        // Validate that the file is a PDF
        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Create and save the document metadata
        DocumentMetadata document = new DocumentMetadata();
        document.setFileName(fileName);
        document.setSubjectName(subjectName);
        document.setCategory(category);
        document.setUploadedByUsername(uploadedByUsername);
        document.setPdfFile(new Binary(file.getBytes())); // Save PDF content as binary

        return documentRepository.save(document); // Save to MongoDB
    }
      /**
     * Query documents based on the provided parameters.
     *
     * @param fileName          (Optional) The file name to search.
     * @param subjectName       (Optional) The subject name to search.
     * @param category          (Optional) The category to search.
     * @param uploadedByUsername (Optional) The username to search.
     * @return List of matching documents.
     */
    public List<DocumentMetadata> queryDocuments(Optional<String> fileName, Optional<String> subjectName, Optional<DocumentCategory> category, Optional<String> uploadedByUsername) {
        // Validate all parameters and only call repository methods with meaningful values
        if (fileName.filter(f -> !f.isBlank()).isPresent() && 
            subjectName.filter(s -> !s.isBlank()).isPresent() && 
            category.isPresent() && 
            uploadedByUsername.filter(u -> !u.isBlank()).isPresent()) {
            // All filters are provided and populated
            return documentRepository.findByFileNameAndSubjectNameAndCategoryAndUploadedByUsername(
                fileName.get().trim(), subjectName.get().trim(), category.get(), uploadedByUsername.get().trim());
        } else if (fileName.filter(f -> !f.isBlank()).isPresent() && 
                   subjectName.filter(s -> !s.isBlank()).isPresent()) {
            // Only fileName and subjectName are provided and populated
            return documentRepository.findByFileNameAndSubjectName(
                fileName.get().trim(), subjectName.get().trim());
        } else if (category.isPresent()) {
            // Only category is provided and populated
            return documentRepository.findByCategory(category.get());
        } else if (uploadedByUsername.filter(u -> !u.isBlank()).isPresent()) {
            // Only uploadedByUsername is provided and populated
            return documentRepository.findByUploadedByUsername(uploadedByUsername.get().trim());
        } else {
            // No meaningful filters provided, return all documents
            return documentRepository.findAll();
        }
    }

    /**
     * Get a document by ID for download.
     *
     * @param documentId The ID of the document to fetch.
     * @return Binary content of the document.
     */
    public byte[] downloadDocumentById(String documentId) {
        Optional<DocumentMetadata> documentOpt = documentRepository.findById(documentId);
        if (documentOpt.isEmpty()) {
            throw new IllegalArgumentException("Document not found for the provided ID: " + documentId);
        }
        return documentOpt.get().getPdfFile().getData();
    }

}