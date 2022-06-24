package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Reply;
import nl.novi.hulppost.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository <Reply, Long> {

    Optional<Reply> findById (Long replyId);
}
