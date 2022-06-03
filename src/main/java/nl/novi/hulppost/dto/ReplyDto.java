package nl.novi.hulppost.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

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
    private String text;
}
