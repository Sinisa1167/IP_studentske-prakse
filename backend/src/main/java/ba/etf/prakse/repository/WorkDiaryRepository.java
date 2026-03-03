package ba.etf.prakse.repository;

import ba.etf.prakse.model.WorkDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkDiaryRepository extends JpaRepository<WorkDiary, Long> {

    List<WorkDiary> findByStudentIdAndInternshipIdOrderByWeekNumber(Long studentId, Long internshipId);

    Optional<WorkDiary> findByStudentIdAndInternshipIdAndWeekNumber(Long studentId, Long internshipId, Integer weekNumber);
}
