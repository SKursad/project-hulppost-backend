package nl.novi.hulppost.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachmentDTO {

    private Long requestId;
    private String name;
    private String fileType;

}
