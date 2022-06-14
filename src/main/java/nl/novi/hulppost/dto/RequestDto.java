package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.enums.TypeRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestDto {

    private Long id;
    private Long accountId;

    @NotEmpty
    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    @Size(min = 4, message = "Titel van de bericht moet minimaal 4 tekens bevatten")
    private String title;
    private TypeRequest typeRequest;

    @NotEmpty
    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    private  String content;
}