package ba.etf.prakse.repository;

import ba.etf.prakse.model.CvEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CvEntryRepository extends JpaRepository<CvEntry, Long> {

    List<CvEntry> findByStudentId(Long studentId);

    List<CvEntry> findByStudentIdAndType(Long studentId, String type);
}
