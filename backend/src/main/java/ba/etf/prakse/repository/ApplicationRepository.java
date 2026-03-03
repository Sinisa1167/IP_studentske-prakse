package ba.etf.prakse.repository;

import ba.etf.prakse.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    Page<Application> findByInternshipId(Long internshipId, Pageable pageable);

    Page<Application> findByInternshipIdAndStatus(Long internshipId, String status, Pageable pageable);

    Optional<Application> findByStudentIdAndInternshipId(Long studentId, Long internshipId);

    boolean existsByStudentIdAndInternshipId(Long studentId, Long internshipId);
}
