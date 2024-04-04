package groupone.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class EmailCredentialsDTO implements Serializable {
    private String host;
    private int port;
    private String email;
    private String username;
    private String password;
}
