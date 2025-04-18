package com.docmanager.controller;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;
import com.docmanager.model.DocumentMetadata.Sem;
import com.docmanager.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * Uploads a document with metadata.
     *
     * @param fileName          Name of the document file.
     * @param subjectName       Subject associated with the document.
     * @param category          Document category.
     * @param uploadedByUsername Username of the uploader.
     * @param sem               Semester associated with the document.
     * @param file              PDF file to upload.
     * @return ResponseEntity with saved document metadata or error message.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("fileName") String fileName,
            @RequestParam("subjectName") String subjectName,
            @RequestParam("category") DocumentCategory category,
            @RequestParam("uploadedByUsername") String uploadedByUsername,
            @RequestParam("sem") Sem sem,
            @RequestParam("file") MultipartFile file) {

        try {
            DocumentMetadata document = documentService.uploadDocument(fileName, subjectName, category, sem, file, uploadedByUsername);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Queries documents based on optional filters.
     *
     * @param fileName          Optional file name filter.
     * @param subjectName       Optional subject name filter.
     * @param category          Optional document category filter.
     * @param uploadedByUsername Optional uploader username filter.
     * @return ResponseEntity with the list of matching documents or error message.
     */
    @GetMapping("/query")
    public ResponseEntity<?> queryDocuments(
            @RequestParam Optional<String> fileName,
            @RequestParam Optional<String> subjectName,
            @RequestParam Optional<DocumentCategory> category,
            @RequestParam Optional<String> uploadedByUsername) {

        try {
            List<DocumentMetadata> documents = documentService.queryDocuments(fileName, subjectName, category, uploadedByUsername);
            return new ResponseEntity<>(documents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Downloads a document by ID.
     *
     * @param id The ID of the document to download.
     * @return ResponseEntity with binary content of the document or error message.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadDocument(@PathVariable String id) {
        try {
            byte[] pdfContent = documentService.downloadDocumentById(id);

            // Set response headers for download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to download document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a document by ID.
     *
     * @param id The ID of the document to delete.
     * @return ResponseEntity with a success message or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id) {
        try {
            documentService.deleteDocumentById(id); // Assuming service method exists
            return new ResponseEntity<>("Document deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the `n` most recently uploaded documents.
     *
     * @param n The number of recent documents to retrieve.
     * @return ResponseEntity with the list of documents or error message.
     */
    @GetMapping("/recent/{n}")
    public ResponseEntity<?> getRecentlyUploadedDocuments(@PathVariable int n) {
        try {
            List<DocumentMetadata> documents = documentService.getRecentlyUploadedDocuments(n);
            return new ResponseEntity<>(documents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve recent documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the `n` most viewed documents.
     *
     * @param n The number of top viewed documents to retrieve.
     * @return ResponseEntity with the list of most viewed documents or error message.
     */
    @GetMapping("/most-viewed/{n}")
    public ResponseEntity<?> getMostViewedDocuments(@PathVariable int n) {
        try {
            List<DocumentMetadata> mostViewedDocuments = documentService.getTopMostViewedDocuments(n);
            return new ResponseEntity<>(mostViewedDocuments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve most viewed documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the `n` most downloaded documents.
     *
     * @param n The number of top downloaded documents to retrieve.
     * @return ResponseEntity with the list of most downloaded documents or error message.
     */
    @GetMapping("/most-downloaded/{n}")
    public ResponseEntity<?> getMostDownloadedDocuments(@PathVariable int n) {
        try {
            List<DocumentMetadata> mostDownloadedDocuments = documentService.getTopMostDownloadedDocuments(n);
            return new ResponseEntity<>(mostDownloadedDocuments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve most downloaded documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the `n` documents based on semester (`Sem`).
     *
     * @param sem The semester to filter by.
     * @param n   The number of documents to retrieve.
     * @return ResponseEntity with the list of documents or error message.
     */
    @GetMapping("/by-sem/{sem}/{n}")
    public ResponseEntity<?> getDocumentsBySemester(@PathVariable Sem sem, @PathVariable int n) {
        try {
            List<DocumentMetadata> documentsBySem = documentService.getDocumentsBySemester(sem, n);
            return new ResponseEntity<>(documentsBySem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve documents by semester: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}