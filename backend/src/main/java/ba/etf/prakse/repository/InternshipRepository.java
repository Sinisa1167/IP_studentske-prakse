package ba.etf.prakse.repository;

import ba.etf.prakse.model.Internship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    Page<Internship> findByActive(Boolean active, Pageable pageable);

    Page<Internship> findByTitleContainingIgnoreCaseAndActive(String title, Boolean active, Pageable pageable);

    Page<Internship> findByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT i FROM Internship i JOIN i.technologies t WHERE t.id = :techId AND i.active = true")
    Page<Internship> findByTechnologyId(@Param("techId") Long techId, Pageable pageable);

    @Query("SELECT i FROM Internship i WHERE i.company.id = :companyId AND i.active = true AND " +
           "i.title LIKE %:title%")
    Page<Internship> findByCompanyIdAndTitleContaining(@Param("companyId") Long companyId,
                                                       @Param("title") String title,
                                                       Pageable pageable);
}
