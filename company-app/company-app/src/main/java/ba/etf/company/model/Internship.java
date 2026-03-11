package ba.etf.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Internship {
    private Long id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String conditions;
    private Boolean active;
    private List<Technology> technologies;
}