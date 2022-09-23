package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.Attachment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestDTO {

    private Long id;
    private Long userId;
//    private String fileId;

    private Attachment attachment;
    @NotEmpty
    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    @Size(min = 4, message = "De titel van de bericht moet minimaal 4 tekens bevatten")
    private String title;
    private String typeRequest;

    @NotEmpty
    @NotNull(message = "Het veld mag niet onbeschreven zijn")
    @NotBlank(message = "Het veld mag niet leeg zijn")
    private String content;

}