package ba.etf.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {
    private Long id;
    private Student student;
    private Internship internship;
    private String status;
    private String appliedAt;
    private List<Evaluation> evaluations;
}