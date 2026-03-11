package ba.etf.company.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CvEntry {
    private Long id;
    private String type;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String extra;
}