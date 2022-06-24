package nl.novi.hulppost.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import nl.novi.hulppost.util.UniqueEmail;
import nl.novi.hulppost.util.UniqueUsername;
import org.hibernate.boot.model.source.internal.hbm.InLineViewSourceImpl;
import org.springframework.web.servlet.View;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long id;

    private Long accountId;

    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    @UniqueUsername
    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    private String username;

    @UniqueEmail
    @Email(message = "{project.constraint.Email.message}")
    private String email;

    @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{project.constraint.emptySpace.Pattern.message}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{project.constraint.password.Pattern.message}")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
