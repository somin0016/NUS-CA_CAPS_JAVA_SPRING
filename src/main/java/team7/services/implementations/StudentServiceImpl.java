package team7.services.implementations;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Student;
import team7.repos.StudentRepository;
import team7.services.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository repoStudent;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Student createStudent(Student student) {
		student.setCreatedAt(OffsetDateTime.now());
		student.setUpdatedAt(OffsetDateTime.now());
		student.setPassword(passwordEncoder.encode(student.getPassword()));
		
		return repoStudent.save(student);
	}
	
	public Student updateStudent(Long id, Student student) {
		return repoStudent.findById(id)
				.map(existingStudent -> {
					existingStudent.setMatricNumber(student.getMatricNumber());
					existingStudent.setFirstName(student.getFirstName());
					existingStudent.setLastName(student.getLastName());
					existingStudent.setGender(student.getGender());
					existingStudent.setDob(student.getDob());
					existingStudent.setPhone(student.getPhone());
					
					return repoStudent.save(existingStudent);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Not Found"));
	}
	
	public Boolean deleteStudent(Long id) {
		return repoStudent.findById(id)
				.map(existingStudent -> {
					repoStudent.delete(existingStudent);
					return true;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<Student> getAllStudents(){
		return repoStudent.findAll();
	}
	
	public Student getStudentById(Long id) {
		return repoStudent.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Student getStudentByEmail(String email) {
		return repoStudent.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
