package ba.etf.prakse.service;

import ba.etf.prakse.model.Student;
import ba.etf.prakse.model.User;
import ba.etf.prakse.repository.StudentRepository;
import ba.etf.prakse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Student> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Page<Student> searchByLastName(String lastName, Pageable pageable) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
    }

    public Optional<Student> getById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    @Transactional
    public Student create(Student student, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("STUDENT");
        user.setActive(true);
        userRepository.save(user);

        student.setUser(user);
        return studentRepository.save(student);
    }

    @Transactional
    public Student update(Long id, Student updated) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));

        student.setFirstName(updated.getFirstName());
        student.setLastName(updated.getLastName());
        student.setEmail(updated.getEmail());
        student.setPhone(updated.getPhone());
        student.setDateOfBirth(updated.getDateOfBirth());
        student.setAddress(updated.getAddress());

        return studentRepository.save(student);
    }

    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public void updatePhoto(Long id, String photoPath) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student nije pronađen"));
        student.setPhotoPath(photoPath);
        studentRepository.save(student);
    }
}
