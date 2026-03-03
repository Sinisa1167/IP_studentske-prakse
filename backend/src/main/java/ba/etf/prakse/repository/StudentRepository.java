package ba.etf.prakse.repository;

import ba.etf.prakse.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    Page<Student> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}
