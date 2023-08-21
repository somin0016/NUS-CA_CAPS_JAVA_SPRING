package team7.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import team7.models.Course;
import team7.models.StudentEnrollment;
import team7.models.Lecturer;
import team7.services.CourseService;
import team7.services.LecturerService;
import team7.services.StudentEnrollmentService;
import team7.validators.CourseValidator;

@Controller
public class CourseController {

	/*** Dependency Injections */
	@Autowired
	private CourseService svcCourse;
	
	@Autowired
	private LecturerService svcLecturer;
	
	@Autowired
	private StudentEnrollmentService svcStudentEnrollment;
	
	@Autowired
	private CourseValidator validCourse;
	
	@InitBinder
	private void initCourseBinder(WebDataBinder binder) {
		binder.addValidators(validCourse);
	}
	
	/*** Methods ***/
	/*** Functional Operations ***/
	public String getStudentsInCourse(Long courseId, Model model) {
		List<StudentEnrollment> enrollments =
				svcStudentEnrollment.getStudentEnrollmentsByCourse(courseId);
		Integer countEnrollments = enrollments.size();
		model.addAttribute("course", svcCourse.getCourseById(courseId));
		model.addAttribute("enrollmentList", enrollments);
		model.addAttribute("countEnrollments", countEnrollments);
		return "student-enrollment-list";
	}
	
	/*** CRUD Operations ***/
	public String getCreateCourse(Model model) {
		model.addAttribute("course", new Course());
		List<Lecturer> lecturers = svcLecturer.getAllLecturers();
		model.addAttribute("lecturerList", lecturers);
		model.addAttribute("action", "create");
		return "course-upsert";
	}

	public String postCreateCourse(
			Course course,
			String redirectURI) {
		svcCourse.createCourse(course);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getUpdateCourse(Long id, Model model) {
		Course updatingCourse = svcCourse.getCourseById(id);
		
		model.addAttribute("course", updatingCourse);
		List<Lecturer> lecturers = svcLecturer.getAllLecturers();
		model.addAttribute("lecturerList", lecturers);
		model.addAttribute("action", "update");
		return "course-upsert";
	}
	
	public String putUpdateCourse(
			Long id,
			Course course,
			String redirectURI) {
		svcCourse.updateCourse(id, course);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String deleteCourse(Long id, String redirectURI) {
		svcCourse.softDeleteCourse(id);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getAllCourses(Model model) {
		List<Course> allCourses = svcCourse.getAllCourses();
		model.addAttribute("courseList", allCourses);
		return "course-list";
	}
}
