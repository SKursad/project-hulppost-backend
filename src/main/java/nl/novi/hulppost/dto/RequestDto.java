package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.enums.TypeRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {

    private Long id;
    private Long accountId;

    @NotEmpty
    @Size(min = 4, message = "Titel van de bericht moet minimaal 4 tekens bevatten")
    private String title;
    private TypeRequest typeRequest;

    @NotEmpty
    private  String content;
}