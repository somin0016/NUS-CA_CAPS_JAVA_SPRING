package team7.services;

import java.util.List;

import team7.models.Lecturer;

public interface LecturerService {

	Lecturer createLecturer(Lecturer lecturer);
	
	Lecturer getLecturerById(Long id);

	Lecturer getLecturerByEmail(String email);
	
	Lecturer updateLecturer(Long id, Lecturer lecturer);
	
	Boolean deleteLecturer(Long id);
	
	List<Lecturer> getAllLecturers();
}
