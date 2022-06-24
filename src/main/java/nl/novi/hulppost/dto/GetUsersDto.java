package nl.novi.hulppost.dto;

import lombok.*;
import nl.novi.hulppost.util.ValidImage;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetUsersDto {

    private Long id;

    private String username;

    private String email;

    @ValidImage
    private String image;
}
