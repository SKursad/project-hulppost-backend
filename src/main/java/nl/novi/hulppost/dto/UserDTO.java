package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.Role;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;

import javax.validation.constraints.*;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Long id;
    private Long requestId;
    private Long replyId;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(min = 4, message = "{hulppost.javax.validation.constraints.username.Size.message}")
    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.emptySpace.username.Pattern.message}")
    private String username;

    @UniqueEmail
    @Email(message = "{hulppost.javax.validation.constraints.Email.message}")
    private String email;

    private String image;

    private Set<Role> roles;

}
