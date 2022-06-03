package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long id;

    private Long accountId;

    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    private String username;

    @UniqueEmail
    @Email(message = "{project.constraint.Email.message}")
    private String email;

    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{project.constraint.password.Pattern.message}")
    private String password;
}
