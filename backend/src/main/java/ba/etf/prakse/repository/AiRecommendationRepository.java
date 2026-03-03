package ba.etf.prakse.repository;

import ba.etf.prakse.model.AiRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Long> {

    List<AiRecommendation> findByStudentIdOrderByScoreDesc(Long studentId);

    void deleteByStudentId(Long studentId);
}
