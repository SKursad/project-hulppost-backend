package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.enums.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccountDto {

    private Long id;

    private Long userId;

    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    private String firstName;

    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    private String surname;
    //    @Pattern(regexp = "^M$|^V$")
    private Gender gender;

    //    @JsonFormat(pattern = "dd-MM-yyyy") // werkt niet met JSON bij het voegen in database
    @Pattern(regexp = "^(3[01]|[12]\\d|0[1-9])/(1[0-2]|0[1-9])/\\d{4}$", message = "{project.constraint.dob.Pattern.message}")
    private String birthday;

    @Pattern(regexp = "^[1-9][0-9]{3}\\s?([a-zA-Z]{2})?$", message = "{project.constraint.zipCode.Pattern.message}")
    private String zipCode;

    private String telNumber;
}