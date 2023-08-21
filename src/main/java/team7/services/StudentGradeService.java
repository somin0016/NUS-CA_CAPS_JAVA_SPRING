package team7.services;

import java.util.List;

import team7.models.StudentGrade;

public interface StudentGradeService {

	StudentGrade getStudentGrade(Long courseId, Long studentId);
	
	List<StudentGrade> getStudentGradesByStudent(Long studentId);
	
	List<StudentGrade> getStudentGradesByLecturer(Long lecturerId);
	
	StudentGrade createStudentGrade(StudentGrade grade);
	
	StudentGrade updateStudentGrade(Long courseId, Long studentId, StudentGrade grade);
}
