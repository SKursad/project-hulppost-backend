package nl.novi.hulppost.util;

import nl.novi.hulppost.model.User;
import nl.novi.hulppost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;


public class UniqueEmailValidator  implements ConstraintValidator<UniqueEmail, String> {

        @Autowired
        UserRepository userRepository;

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            Optional<User> user = userRepository.findByEmail(email);
            return user.isEmpty();
        }
}
