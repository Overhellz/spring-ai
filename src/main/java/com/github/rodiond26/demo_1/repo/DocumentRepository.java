package com.github.rodiond26.demo_1.repo;

import com.github.rodiond26.demo_1.model.LoadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<LoadedDocument, Long> {
    boolean existsByFilenameAndContentHash(String filename, String contentHash);
}
