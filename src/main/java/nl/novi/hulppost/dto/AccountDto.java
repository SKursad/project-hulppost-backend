package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.enums.Gender;

import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String surname;
//    @Pattern(regexp = "^M$|^V$")
    private Gender gender;
    @Pattern(regexp = "^(3[01]|[12]\\d|0?[1-9])/(1[0-2]|0?[1-9])/(?:\\d{2})?\\d{2}$", message = "{project.constraint.dob.Pattern.message}")
    private String birthday;
    @Pattern(regexp = "^[1-9][0-9]{3}\\s?([a-zA-Z]{2})?$", message = "{project.constraint.zipCode.Pattern.message}")
    private String zipCode;
    private String telNumber;
}