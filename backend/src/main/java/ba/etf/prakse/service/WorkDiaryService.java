package ba.etf.prakse.service;

import ba.etf.prakse.model.Internship;
import ba.etf.prakse.model.Student;
import ba.etf.prakse.model.WorkDiary;
import ba.etf.prakse.repository.InternshipRepository;
import ba.etf.prakse.repository.StudentRepository;
import ba.etf.prakse.repository.WorkDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkDiaryService {

    private final WorkDiaryRepository workDiaryRepository;
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;

    public List<WorkDiary> getByStudentAndInternship(Long studentId, Long internshipId) {
        return workDiaryRepository.findByStudentIdAndInternshipIdOrderByWeekNumber(studentId, internshipId);
    }

    public Optional<WorkDiary> getById(Long id) {
        return workDiaryRepository.findById(id);
    }

    @Transactional
    public WorkDiary save(Long studentId, Long internshipId, WorkDiary entry) {
        if (workDiaryRepository.findByStudentIdAndInternshipIdAndWeekNumber(
                studentId, internshipId, entry.getWeekNumber()).isPresent()) {
            throw new RuntimeException("Unos za ovu sedmicu već postoji");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Praksa nije pronađena"));

        entry.setStudent(student);
        entry.setInternship(internship);
        return workDiaryRepository.save(entry);
    }

    @Transactional
    public WorkDiary update(Long id, WorkDiary updated) {
        WorkDiary entry = workDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unos nije pronađen"));
        entry.setActivities(updated.getActivities());
        return workDiaryRepository.save(entry);
    }

    @Transactional
    public void delete(Long id) {
        workDiaryRepository.deleteById(id);
    }
}
