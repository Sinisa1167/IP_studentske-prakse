package ba.etf.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Evaluation {
    private Long id;
    private String evaluatorRole;
    private Integer grade;
    private String comment;
    private String createdAt;
}