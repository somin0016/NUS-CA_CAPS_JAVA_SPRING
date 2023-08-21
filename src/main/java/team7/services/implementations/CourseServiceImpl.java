package team7.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Course;
import team7.models.StudentEnrollment;
import team7.repos.CourseRepository;
import team7.repos.StudentEnrollmentRepository;
import team7.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	CourseRepository repoCourse;
	
	@Autowired
	StudentEnrollmentRepository repoStudentEnrollment;

	public Course createCourse(Course course) {
		return repoCourse.save(course);
	}
	
	public Course updateCourse(Long id, Course course) {
		return repoCourse.findById(id)
				.map(existingCourse -> {
					existingCourse.setId(existingCourse.getId());
					existingCourse.setCourseName(course.getCourseName());
					existingCourse.setDescription(course.getDescription());
					existingCourse.setCourseCredits(course.getCourseCredits());
					existingCourse.setCourseCapacity(course.getCourseCapacity());
					existingCourse.setCourseFee(course.getCourseFee());
					existingCourse.setCourseStartDate(course.getCourseStartDate());
					existingCourse.setCourseDuration(course.getCourseDuration());
					existingCourse.setCourseStatus(course.getCourseStatus());
					existingCourse.setEnrollment(course.getEnrollment());
					existingCourse.setLecturer(course.getLecturer());
					
					return repoCourse.save(existingCourse);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Not Found"));
	}
	
	public Boolean softDeleteCourse(Long id) {
		Course course =
				repoCourse.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		for (StudentEnrollment enrollment: course.getEnrollment()) {
			enrollment.setEnrollmentStatus(3);
			repoStudentEnrollment.save(enrollment);
		}
		
		course.setCourseStatus(4);
		repoCourse.save(course);
		
		return true;
	}
	
	public List<Course> getAllCourses(){
		List<Course> allCourses = repoCourse.findAll();
		return allCourses.stream()
				.map(course -> {
					return getDetailedCourse(course);
				}).toList();
	}
	
	public Course getCourseById(Long id) {
		return repoCourse.findById(id)
				.map(course -> getDetailedCourse(course))
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<Course> getAvailableCoursesByStudent(Long studentId){
		return repoStudentEnrollment.findCoursesNotInEnrollment(studentId);
	}
	
	private Long getStudentCountInCourse(Long id) {
		Course course =
				repoCourse.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		return course.getEnrollment().stream()
				.map(cs -> cs.getStudent()).count();
	}
	
	public Course getDetailedCourse(Course course){
		Long studentCount = getStudentCountInCourse(course.getId());
		Integer enrollmentStartDays = 30;
		Integer enrollmentEndDays = 10;
		
		course.setCourseVacancy(
				course.getCourseCapacity() - studentCount);
		course.setCourseEndDate(
				course.getCourseStartDate().plusDays(course.getCourseDuration()));
		course.setEnrollmentStartDate(
				course.getCourseStartDate().minusDays(enrollmentStartDays));
		course.setEnrollmentEndDate(
				course.getCourseStartDate().minusDays(enrollmentEndDays));
		
		
		return course;
	}
	
	public Course updateCourseStatus(Long id, Integer status) {
		Course course = repoCourse.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		course.setCourseStatus(status);
		return repoCourse.save(course);
	}
}
