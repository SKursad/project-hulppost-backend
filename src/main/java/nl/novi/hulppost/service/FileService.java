package nl.novi.hulppost.service;

import nl.novi.hulppost.model.FileAttachment;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileAttachment saveAttachment(MultipartFile file, Long requestId) throws Exception;

    String detectType(byte[] decodedBytes);

    String saveProfileImage(String base64Image) throws Exception;

    void  deleteProfileImage(String image);

    void deleteAttachmentImage(String image);
}
