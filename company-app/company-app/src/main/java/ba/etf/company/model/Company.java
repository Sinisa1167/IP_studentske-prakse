package ba.etf.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
    private Long id;
    private String name;
    private String address;
    private String contactEmail;
    private String description;
    private Boolean active;
    private User user;
}