package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.model.FileAttachment;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequestDTO {

    private Long id;
    private Long userId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private FileAttachment fileAttachment;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(min = 4,max = 50, message = "{hulppost.javax.validation.constraints.title.Size.message}")
    private String title;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    private String typeRequest;

    @NotBlank(message = "{hulppost.javax.validation.constraints.NotBlank.message}")
    @Size(max = 2500, message = "{hulppost.javax.validation.constraints.content.Size.message}")
    private String content;

}