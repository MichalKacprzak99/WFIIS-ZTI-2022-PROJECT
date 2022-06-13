package zti.restaurantmatcher.auth;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthCredentials {

    private String email;
    private String password;

}