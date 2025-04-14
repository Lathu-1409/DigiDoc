// package com.docmanager.controller;

// import com.docmanager.model.DocumentMetadata;
// import com.docmanager.model.DocumentMetadata.Sem;
// import com.docmanager.service.DocumentService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/manage-documents")
// public class DocumentManagementController {

//     @Autowired
//     private DocumentService documentService;

//     /**
//      * Deletes a document by its ID.
//      *
//      * @param id The ID of the document to delete.
//      * @return ResponseEntity with a success message or error message.
//      */
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteDocument(@PathVariable String id) {
//         try {
//             documentService.deleteDocumentById(id); // Assuming service method exists
//             return new ResponseEntity<>("Document deleted successfully.", HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to delete document: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     /**
//      * Retrieves the `n` most recently uploaded documents.
//      *
//      * @param n The number of recent documents to retrieve.
//      * @return ResponseEntity with the list of documents or error message.
//      */
//     @GetMapping("/recent/{n}")
//     public ResponseEntity<?> getRecentlyUploadedDocuments(@PathVariable int n) {
//         try {
//             List<DocumentMetadata> documents = documentService.getRecentlyUploadedDocuments(n); // Assuming service method exists
//             return new ResponseEntity<>(documents, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to retrieve recent documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     /**
//      * Retrieves the `n` most viewed documents.
//      *
//      * @param n The number of top viewed documents to retrieve.
//      * @return ResponseEntity with the list of most viewed documents or error message.
//      */
//     @GetMapping("/most-viewed/{n}")
//     public ResponseEntity<?> getMostViewedDocuments(@PathVariable int n) {
//         try {
//             List<DocumentMetadata> mostViewedDocuments = documentService.getTopMostViewedDocuments(n);
//             return new ResponseEntity<>(mostViewedDocuments, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to retrieve most viewed documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     /**
//      * Retrieves the `n` most downloaded documents.
//      *
//      * @param n The number of top downloaded documents to retrieve.
//      * @return ResponseEntity with the list of most downloaded documents or error message.
//      */
//     @GetMapping("/most-downloaded/{n}")
//     public ResponseEntity<?> getMostDownloadedDocuments(@PathVariable int n) {
//         try {
//             List<DocumentMetadata> mostDownloadedDocuments = documentService.getTopMostDownloadedDocuments(n);
//             return new ResponseEntity<>(mostDownloadedDocuments, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to retrieve most downloaded documents: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     /**
//      * Retrieves the `n` documents based on semester (`Sem`).
//      *
//      * @param sem The semester to filter by.
//      * @param n   The number of documents to retrieve.
//      * @return ResponseEntity with the list of documents or error message.
//      */
//     @GetMapping("/by-sem/{sem}/{n}")
//     public ResponseEntity<?> getDocumentsBySemester(@PathVariable Sem sem, @PathVariable int n) {
//         try {
//             List<DocumentMetadata> documentsBySem = documentService.getDocumentsBySemester(sem, n);
//             return new ResponseEntity<>(documentsBySem, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>("Failed to retrieve documents by semester: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }
// }