package nl.novi.hulppost.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;
    private Long accountId;
    private String username;
    private String email;
    private String password;

}
