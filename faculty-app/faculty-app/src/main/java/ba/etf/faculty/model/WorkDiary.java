package ba.etf.faculty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkDiary {
    private Long id;
    private Integer weekNumber;
    private String activities;
    private String createdAt;
}