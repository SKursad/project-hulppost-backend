package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByDateBeforeAndRequestIsNull(Date date);

    Optional<Attachment> findById(Long fileId);

}
