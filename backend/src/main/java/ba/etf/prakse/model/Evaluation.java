package ba.etf.prakse.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "evaluator_role", nullable = false, length = 20)
    private String evaluatorRole; // FACULTY, COMPANY

    private Integer grade;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false)
private LocalDateTime createdAt;

@PrePersist
protected void onCreate() {
    if (createdAt == null) createdAt = LocalDateTime.now();
}
}
