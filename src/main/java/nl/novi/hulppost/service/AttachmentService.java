package nl.novi.hulppost.service;

import nl.novi.hulppost.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    void deleteAttachment(String id);

    String detectType(byte[] decodedBytes);

}
