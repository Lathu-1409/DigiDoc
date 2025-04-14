package com.docmanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import org.bson.types.Binary;
@Data
@Document(collection = "documents")
public class DocumentMetadata {

    public enum DocumentCategory {
        NOTES,
        QUESTION_PAPER,
        STUDY_MATERIAL
    }
    public enum Sem {
        I,
        II,
        III,
        IV,
        V,
        VI,
        VII,
        VIII
    }
    
    @Id
    private String id; // Unique identifier
    private String fileName; // Name of the uploaded file
    private String subjectName; // Subject associated with the file
    private DocumentCategory category;
    private Sem sem; // Enum for document category
    private Binary pdfFile; // PDF file stored as binary data
    private String uploadedByUsername; // Username of the user who uploaded this document
    private long viewCount;
    private long downloadCount;
}