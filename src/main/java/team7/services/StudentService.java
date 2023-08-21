package team7.services;

import java.util.List;

import team7.models.Student;

public interface StudentService {

	Student createStudent(Student student);
	
	Student getStudentById(Long id);
	
	Student getStudentByEmail(String email);
	
	Student updateStudent(Long id, Student student);
	
	Boolean deleteStudent(Long id);
	
	List<Student> getAllStudents(); 
}
