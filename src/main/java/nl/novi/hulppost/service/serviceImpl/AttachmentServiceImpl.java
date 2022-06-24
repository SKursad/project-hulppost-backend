package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.exception.ResourceNotFoundException;
import nl.novi.hulppost.model.Attachment;
import nl.novi.hulppost.repository.AttachmentRepository;
import nl.novi.hulppost.service.AttachmentService;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final Tika tika;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.tika = new Tika();
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")) {
                throw  new Exception("De bestandsnaam bevat tekens die niet zijn toegestaan. "
                        + fileName);
            }

            Attachment attachment
                    = new Attachment(fileName,
                    file.getContentType(),
                    file.getBytes());
            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Kon het bestand niet opslaan.: " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Bestand niet gevonden met de Id: " + fileId));
    }

    @Override
    public String detectType(byte[] fileArr) {
        return tika.detect(fileArr);
    }

    @Override
    public void deleteAttachment(String id) {
        attachmentRepository.deleteById(id);
    }

}
