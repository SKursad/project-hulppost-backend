package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByDateBeforeAndRequestIsNull(Date date);

    Optional<FileAttachment> findById(Long fileId);

}
