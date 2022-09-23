package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;
import nl.novi.hulppost.util.ValidImage;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Long id;
    private Long requestId;
    private Long replyId;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    @Size(min = 4, message = "De gebruikersnaam moet minimaal 4 karakters bevatten")
    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    private String username;

    @UniqueEmail
    @Email(message = "{project.constraint.Email.message}")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String email;

    @ValidImage
    private String image;

}
