package com.docmanager.controller;

import com.docmanager.model.DocumentMetadata;
import com.docmanager.model.DocumentMetadata.DocumentCategory;
import com.docmanager.service.DocumentService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("fileName") String fileName,
            @RequestParam("subjectName") String subjectName,
            @RequestParam("category") DocumentCategory category,
            @RequestParam("uploadedByUsername") String uploadedByUsername,
            @RequestParam("file") MultipartFile file) {

        try {
            DocumentMetadata document = documentService.uploadDocument(fileName, subjectName, category, file, uploadedByUsername);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

      @GetMapping("/query")
    public ResponseEntity<?> queryDocuments(
            @RequestParam Optional<String> fileName,
            @RequestParam Optional<String> subjectName,
            @RequestParam Optional<DocumentCategory> category,
            @RequestParam Optional<String> uploadedByUsername) {
        try {
            System.out.println(fileName);
            System.out.println(subjectName);
            System.out.println(category);
            List<DocumentMetadata> documents = documentService.queryDocuments(fileName, subjectName, category, uploadedByUsername);
            System.out.println(documents);
            return new ResponseEntity<>(documents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
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

}