package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;

import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    private Long accountId;

    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.username.Pattern.message}")
    private String username;

    @UniqueEmail
    private String email;

    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.username.Pattern.message}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{project.constraint.password.Pattern.message}")
    private String password;

}
