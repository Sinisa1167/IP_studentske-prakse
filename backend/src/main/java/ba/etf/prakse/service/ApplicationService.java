package ba.etf.prakse.service;

import ba.etf.prakse.model.Application;
import ba.etf.prakse.model.Internship;
import ba.etf.prakse.model.Student;
import ba.etf.prakse.repository.ApplicationRepository;
import ba.etf.prakse.repository.InternshipRepository;
import ba.etf.prakse.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;

    public List<Application> getByStudentId(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public Page<Application> getByInternshipId(Long internshipId, Pageable pageable) {
        return applicationRepository.findByInternshipId(internshipId, pageable);
    }

    public Page<Application> getByInternshipIdAndStatus(Long internshipId, String status, Pageable pageable) {
        return applicationRepository.findByInternshipIdAndStatus(internshipId, status, pageable);
    }

    public Optional<Application> getById(Long id) {
        return applicationRepository.findById(id);
    }

    @Transactional
    public Application apply(Long studentId, Long internshipId) {
        if (applicationRepository.existsByStudentIdAndInternshipId(studentId, internshipId)) {
            throw new RuntimeException("Student je već aplicirao na ovu praksu");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Praksa nije pronađena"));

        Application application = new Application();
        application.setStudent(student);
        application.setInternship(internship);
        application.setStatus("PENDING");

        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateStatus(Long id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prijava nije pronađena"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    @Transactional
    public void delete(Long id) {
        applicationRepository.deleteById(id);
    }
}
