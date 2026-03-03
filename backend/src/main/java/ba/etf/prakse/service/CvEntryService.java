package ba.etf.prakse.service;

import ba.etf.prakse.model.CvEntry;
import ba.etf.prakse.model.Student;
import ba.etf.prakse.repository.CvEntryRepository;
import ba.etf.prakse.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CvEntryService {

    private final CvEntryRepository cvEntryRepository;
    private final StudentRepository studentRepository;

    public List<CvEntry> getByStudentId(Long studentId) {
        return cvEntryRepository.findByStudentId(studentId);
    }

    public List<CvEntry> getByStudentIdAndType(Long studentId, String type) {
        return cvEntryRepository.findByStudentIdAndType(studentId, type);
    }

    public Optional<CvEntry> getById(Long id) {
        return cvEntryRepository.findById(id);
    }

    @Transactional
    public CvEntry create(Long studentId, CvEntry entry) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));
        entry.setStudent(student);
        return cvEntryRepository.save(entry);
    }

    @Transactional
    public CvEntry update(Long id, CvEntry updated) {
        CvEntry entry = cvEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CV stavka nije pronađena"));

        entry.setType(updated.getType());
        entry.setTitle(updated.getTitle());
        entry.setDescription(updated.getDescription());
        entry.setStartDate(updated.getStartDate());
        entry.setEndDate(updated.getEndDate());
        entry.setExtra(updated.getExtra());

        return cvEntryRepository.save(entry);
    }

    @Transactional
    public void delete(Long id) {
        cvEntryRepository.deleteById(id);
    }
}
