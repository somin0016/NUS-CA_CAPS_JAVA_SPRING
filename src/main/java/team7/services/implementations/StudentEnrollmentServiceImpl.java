package team7.services.implementations;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Course;
import team7.models.StudentEnrollment;
import team7.models.StudentGrade;
import team7.repos.StudentEnrollmentRepository;
import team7.repos.StudentGradeRepository;
import team7.services.CourseService;
import team7.services.StudentEnrollmentService;
import team7.services.StudentService;

@Service
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

	@Autowired
	StudentEnrollmentRepository repoStudentEnrollment;
	
	@Autowired
	StudentGradeRepository repoStudentGrade;
	
	@Autowired
	CourseService svcCourse;
	
	@Autowired
	StudentService svcStudent;
	
	public List<StudentEnrollment> getAllEnrollments(){
		return repoStudentEnrollment.findAll();
	}
	
	public StudentEnrollment getEnrollmentByCourseAndStudent(Long courseId, Long studentId) {
		return repoStudentEnrollment.findByCourseIdAndStudentId(courseId, studentId)
				.map(enroll -> {
					enroll.setCourse(svcCourse.getDetailedCourse(enroll.getCourse()));
					return enroll;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<StudentEnrollment> getStudentEnrollmentsByCourse(Long courseId){
		return repoStudentEnrollment.findByCourseId(courseId).stream()
			.map(enroll -> {
				StudentGrade grade =
						repoStudentGrade
						.findByCourseIdAndStudentId(courseId, enroll.getStudent().getId())
						.orElse(new StudentGrade());
				
				enroll.setStudentGrade(grade.getStudentGrade());
				enroll.setStudentCredits(grade.getStudentCredits());
				enroll.setCourse(svcCourse.getDetailedCourse(enroll.getCourse()));
				
				return enroll;
			}).toList();
	}
	
	public List<Course> getAvailableCoursesByStudent(Long studentId){
		return repoStudentEnrollment.findCoursesNotInEnrollment(studentId).stream()
				.map(course -> {
					return svcCourse.getDetailedCourse(course);
				}).toList();
	}
	
	public List<StudentEnrollment> getEnrolledCoursesByStudent(Long studentId) {
		return repoStudentEnrollment.findByStudentId(studentId).stream()
				.map(enroll -> {
					enroll.setCourse(svcCourse.getDetailedCourse(enroll.getCourse()));
					return enroll;
				}).toList();
	}
	
	public StudentEnrollment saveStudentEnrollment(Long courseId, Long studentId) {
		StudentEnrollment enrollment = new StudentEnrollment();
		enrollment.setCourse(svcCourse.getCourseById(courseId));
		enrollment.setStudent(svcStudent.getStudentById(studentId));
		enrollment.setEnrollmentDate(LocalDate.now());
		
		return repoStudentEnrollment.save(enrollment);
	}
	
	public Boolean updateEnrollmentStatus(Long courseId, Long studentId, Integer status) {
		return repoStudentEnrollment.findByCourseIdAndStudentId(courseId, studentId)
				.map(enroll -> {
					enroll.setEnrollmentStatus(status);
					repoStudentEnrollment.save(enroll);
					return true;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
