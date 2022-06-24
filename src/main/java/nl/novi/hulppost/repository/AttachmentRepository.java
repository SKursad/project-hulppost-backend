package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Attachment;
import nl.novi.hulppost.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

    Attachment findByFileName(String fileName);
}
