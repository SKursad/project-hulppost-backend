package nl.novi.hulppost.security;

import nl.novi.hulppost.model.*;
import nl.novi.hulppost.repository.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class MethodLevelSecurityService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ReplyRepository replyRepository;


    public MethodLevelSecurityService(UserRepository userRepository, AccountRepository accountRepository, RequestRepository requestRepository, ReplyRepository replyRepository) {
        super();
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.requestRepository = requestRepository;
        this.replyRepository = replyRepository;
    }

    public boolean hasAuthToChangeRequest(Long requestId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            return request.getUser().getId().equals(user.getId());
        }
        return false;
    }


    public boolean hasAuthToChangeReply(Long replyId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Reply> optionalReply = replyRepository.findById(replyId);
        if (optionalReply.isPresent()) {
            Reply reply = optionalReply.get();
            return reply.getUser().getId() == user.getId();
        }
        return false;
    }

    public boolean isAuthorizedUser(Long userId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User legitUser = optionalUser.get();
            return legitUser.getId() == user.getId();
        }
        return false;
    }

    public boolean isAuthorizedAccount(Long accountId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account legitAccount = optionalAccount.get();
            return legitAccount.getUser().getId() == user.getId();
        }
        return false;
    }

}
