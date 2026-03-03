package ba.etf.prakse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username je obavezan")
    private String username;

    @NotBlank(message = "Password je obavezan")
    private String password;
}
