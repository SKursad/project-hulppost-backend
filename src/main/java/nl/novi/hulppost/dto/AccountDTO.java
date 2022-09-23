package nl.novi.hulppost.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccountDTO {

    private Long id;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String firstName;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String surname;

    private String gender;

    //    @JsonFormat(pattern = "dd-MM-yyyy") // werkt niet met JSON bij het voegen in database
    @NotNull
    @Pattern(regexp = "^(3[01]|[12]\\d|0[1-9])/(1[0-2]|0[1-9])/\\d{4}$", message = "{project.constraint.dob.Pattern.message}")
    private String birthday;

    @Pattern(regexp = "^[1-9][0-9]{3}\\s?([a-zA-Z]{2})?$", message = "{project.constraint.zipCode.Pattern.message}")
    private String zipCode;

}