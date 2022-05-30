package com.actividadaprendizaje.bookshelter.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service para gestión de ficheros
 */
public interface FileService {
    void uploadFile(MultipartFile file);
}
