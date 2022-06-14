package nl.novi.hulppost.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReplyDto {

    private Long id;
    private Long accountId;
    private Long requestId;

    @NotEmpty
    @NotNull(message = "veld mag niet onbeschreven zijn")
    @NotBlank(message = "veld mag niet leeg zijn")
    private String text;
}
