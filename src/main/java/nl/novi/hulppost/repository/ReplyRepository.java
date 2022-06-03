package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository <Reply, Long> {

    List<Reply> findByUserIdAndRequestId(Long userId, Long requestId);

    List<Reply> findByUserId(Long userId);

    List<Reply> findByRequestId(Long requestId);
}
