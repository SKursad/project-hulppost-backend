package nl.novi.hulppost.service.serviceImpl;

import nl.novi.hulppost.config.AppConfiguration;
import nl.novi.hulppost.dto.AttachmentDTO;
import nl.novi.hulppost.model.Attachment;
import nl.novi.hulppost.model.Request;
import nl.novi.hulppost.repository.AttachmentRepository;
import nl.novi.hulppost.repository.RequestRepository;
import nl.novi.hulppost.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@EnableScheduling
public class FileServiceImpl implements FileService {

    AppConfiguration appConfiguration;

    Tika tika;

    @Autowired
    AttachmentRepository attachmentRepository;

    RequestRepository requestRepository;

    public FileServiceImpl(AppConfiguration appConfiguration, AttachmentRepository attachmentRepository, RequestRepository requestRepository) {
        super();
        this.appConfiguration = appConfiguration;
        this.attachmentRepository = attachmentRepository;
        this.tika = new Tika();
        this.requestRepository = requestRepository;
    }

    public String saveProfileImage(String base64Image) throws IOException {
        String imageName = getRandomName();

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        File target = new File(appConfiguration.getFullProfileImagesPath() +"/"+ imageName);
        FileUtils.writeByteArrayToFile(target, decodedBytes);
        return imageName;
    }

    private String getRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String detectType(byte[] fileArr) {
        return tika.detect(fileArr);
    }

    public void deleteProfileImage(String image) {
        try {
            Files.deleteIfExists(Paths.get(appConfiguration.getFullProfileImagesPath() +"/"+ image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Attachment saveAttachment(MultipartFile file, Long requestId) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        Attachment attachment = new Attachment();

        if (optionalRequest.isPresent()) {
            attachment.setRequest(optionalRequest.get());
        }
        attachment.setDate(new Date());
        String randomName = getRandomName();
        attachment.setName(randomName);

        File target = new File(appConfiguration.getFullAttachmentsPath() +"/"+ randomName);
        try {
            byte[] fileAsByte = file.getBytes();
            FileUtils.writeByteArrayToFile(target, fileAsByte);
            attachment.setFileType(detectType(fileAsByte));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return attachmentRepository.save(attachment);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanupStorage() {
        Date oneHourAgo = new Date(System.currentTimeMillis() - (60*60*1000));
        List<Attachment> oldFiles = attachmentRepository.findByDateBeforeAndRequestIsNull(oneHourAgo);
        for(Attachment file: oldFiles) {
            deleteAttachmentImage(file.getName());
            attachmentRepository.deleteById(file.getId());
        }
    }

    public void deleteAttachmentImage(String image) {
        try {
            Files.deleteIfExists(Paths.get(appConfiguration.getFullAttachmentsPath()+"/"+image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public AttachmentDTO mapToDto(Attachment attachment) {
//        AttachmentDTO fileAttachmentDTO = new AttachmentDTO();
//
//        fileAttachmentDTO.setRequestId(attachment.getRequest().getId());
//        fileAttachmentDTO.setName(attachment.getName());
//        fileAttachmentDTO.setFileType(attachment.getFileType());
//
//        return fileAttachmentDTO;
//
//    }
//
//    public Attachment mapToEntity(AttachmentDTO attachmentDto) {
//        Attachment attachment = new Attachment();
//
//        attachment.setRequest(new Request().getAttachment().getRequest());
//        attachment.setName(attachmentDto.getName());
//        attachment.setFileType(attachmentDto.getFileType());
//
//        return attachment;
//    }

}
