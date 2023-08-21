package team7.services.implementations;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Lecturer;
import team7.repos.LecturerRepository;
import team7.services.LecturerService;

@Service
public class LecturerServiceImpl implements LecturerService {

	@Autowired
	LecturerRepository repoLecturer;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Lecturer createLecturer(Lecturer lecturer) {
		lecturer.setCreatedAt(OffsetDateTime.now());
		lecturer.setUpdatedAt(OffsetDateTime.now());
		lecturer.setPassword(passwordEncoder.encode(lecturer.getPassword()));
		
		return repoLecturer.save(lecturer);
	}
	
	public Lecturer updateLecturer(Long id, Lecturer lecturer) {
		return repoLecturer.findById(id)
				.map(existingLecturer -> {
					existingLecturer.setCode(lecturer.getCode());
					existingLecturer.setTitle(lecturer.getTitle());
					existingLecturer.setFirstName(lecturer.getFirstName());
					existingLecturer.setLastName(lecturer.getLastName());
					existingLecturer.setDesignation(lecturer.getDesignation());
					existingLecturer.setPhone(lecturer.getPhone());
					
					return repoLecturer.save(existingLecturer);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Not Found"));
	}
	
	public Boolean deleteLecturer(Long id) {
		return repoLecturer.findById(id)
				.map(existingLecturer -> {
					repoLecturer.delete(existingLecturer);
					return true;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<Lecturer> getAllLecturers(){
		return repoLecturer.findAll();
	}
	
	public Lecturer getLecturerById(Long id) {
		return repoLecturer.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Lecturer getLecturerByEmail(String email) {
		return repoLecturer.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
