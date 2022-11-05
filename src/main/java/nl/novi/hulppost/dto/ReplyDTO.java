package nl.novi.hulppost.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReplyDTO {

    private Long id;
    private Long userId;
    private Long requestId;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
