package com.cine.sk.cinesk.domain.file;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileManagerService {
    public abstract Page<File> list(Pageable pageable, String pathId);
    public abstract Optional<File> get(String fileId, String pathId);
    public abstract File upload(MultipartFile file, String pathId, String id, String name);
    public abstract void delete(String fileId, String pathId);
}
