package nl.novi.hulppost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;

import javax.validation.constraints.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RegistrationDTO {

    private Long id;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(min = 4, message = "{hulppost.javax.validation.constraints.username.Size.message}")
    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.emptySpace.username.Pattern.message}")
    private String username;

    @UniqueEmail
    @Email(message = "{hulppost.javax.validation.constraints.Email.message}")
    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String email;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.emptySpace.pass.Pattern.message}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{hulppost.constraints.password.Pattern.message}")
    private String password;

    private String image;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(min = 4, message = "{hulppost.javax.validation.constraints.firstname.Size.message}")
    private String firstName;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(min = 4, message = "{hulppost.javax.validation.constraints.surname.Size.message}")
    private String surname;

    //    @Pattern(regexp = "^M$|^V$")
    //    @Enumerated(EnumType.STRING)
    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.gen.emptySpace.Pattern.message}")
    private String gender;

    //    @Pattern(regexp = "^(3[01]|[12]\\d|0[1-9])/(1[0-2]|0[1-9])/\\d{4}$", message = "{hulppost.constraints.dob.Pattern.message}")
    @NotNull(message = "{hulppost.javax.validation.constraints.dob.NotNull.message}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Pattern(regexp = "^[1-3][0-9]{3}\\s?([a-zA-Z]{2})\\s?", message = "{hulppost.constraints.zipCode.Pattern.message}")
    private String zipCode;
}
