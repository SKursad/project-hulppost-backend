package nl.novi.hulppost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AccountDTO {

    private Long id;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String firstName;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String surname;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.gen.emptySpace.Pattern.message}")
    private String gender;

    //    @Pattern(regexp = "^(3[01]|[12]\\d|0[1-9])/(1[0-2]|0[1-9])/\\d{4}$", message = "{project.constraint.dob.Pattern.message}")
    @NotNull(message = "{hulppost.javax.validation.constraints.dob.NotEmpty.message}")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    @Pattern(regexp = "^[1-9][0-9]{3}\\s?([a-zA-Z]{2})?$", message = "{hulppost.constraints.zipCode.Pattern.message}")
    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String zipCode;

}