package team7.services;

import java.util.List;

import team7.models.Course;
import team7.models.StudentEnrollment;

public interface StudentEnrollmentService {
	
	List<StudentEnrollment> getAllEnrollments();
	
	StudentEnrollment getEnrollmentByCourseAndStudent(Long courseId, Long studentId);
	
	List<StudentEnrollment> getStudentEnrollmentsByCourse(Long courseId);
	
	List<Course> getAvailableCoursesByStudent(Long studentId);
	
	List<StudentEnrollment> getEnrolledCoursesByStudent(Long studentId);
	
	StudentEnrollment saveStudentEnrollment(Long courseId, Long studentId);
	
	Boolean updateEnrollmentStatus(Long courseId, Long studentId, Integer status);
}
