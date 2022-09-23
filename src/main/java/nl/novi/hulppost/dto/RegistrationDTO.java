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
public class RegistrationDTO {

    private Long id;

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

    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{project.constraint.password.Pattern.message}")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String firstName;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String surname;
    //    @Pattern(regexp = "^M$|^V$")
//    @NotNull(message = "Het veld mag niet onbeschreven zijn")
//    @Enumerated(EnumType.STRING)
    @NotBlank(message = "geslacht")
    private String gender;

    //    @JsonFormat(pattern = "dd-MM-yyyy") // werkt niet met JSON bij het voegen in database
    @Pattern(regexp = "^(3[01]|[12]\\d|0[1-9])/(1[0-2]|0[1-9])/\\d{4}$", message = "{project.constraint.dob.Pattern.message}")
    private String birthday;

    @Pattern(regexp = "^[1-9][0-9]{3}\\s?([a-zA-Z]{2})?$", message = "{project.constraint.zipCode.Pattern.message}")
    private String zipCode;
}
