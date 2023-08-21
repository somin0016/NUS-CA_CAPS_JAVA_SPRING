package team7.controllers;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team7.models.Course;
import team7.models.Student;
import team7.models.StudentEnrollment;
import team7.models.StudentGrade;
import team7.services.CourseService;
import team7.services.StudentEnrollmentService;
import team7.services.StudentGradeService;
import team7.services.StudentService;

@Controller
@RequestMapping("/student/{id}")
public class StudentController {
	
	/*** Dependency Injections ***/
	@Autowired
	StudentService svcStudent;
	
	@Autowired
	CourseService svcCourse;
	
	@Autowired
	StudentEnrollmentService svcStudentEnrollment;
	
	@Autowired
	StudentGradeService svcStudentGrade;
	
	@Autowired
	CourseController ctrlCourse;
	
	/*** Methods ***/
	/*** Functional Operations ***/
	@GetMapping("")
	public String getStudentDashboard(@PathVariable Long id, Model model) {
		Student existingStudent = svcStudent.getStudentById(id);
		List<Course> courses = svcCourse.getAllCourses().stream()
								.filter(course -> course.getCourseStatus() != 4)
								.sorted(Comparator.comparing(Course::getCourseStartDate).reversed())
								.limit(5)
								.toList();
		List<Course> enrolledCourses = existingStudent.getStudentEnrollment().stream()
								.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
								.map(StudentEnrollment::getCourse)
								.sorted(Comparator.comparing(Course::getCourseStartDate).reversed())
								.limit(5)
								.toList();
		Integer totalCourses = Math.toIntExact(svcCourse.getAllCourses().stream()
										.filter(course -> course.getCourseStatus() != 4)
										.count());
		Integer totalEnrolledCourses = Math.toIntExact(existingStudent.getStudentEnrollment().stream()
										.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
										.map(StudentEnrollment::getCourse)
										.count());

		model.addAttribute("student", existingStudent);
		model.addAttribute("courses", courses);
		model.addAttribute("enrolledCourses", enrolledCourses);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("totalEnrolledCourses", totalEnrolledCourses);
		return "dashboard";
	}
	
	@GetMapping("/course")
	public String getAvailableCourses(
			@PathVariable Long id,
			@RequestParam(value = "search", required = false) String search,
			Model model) {
		List<Course> availableCourses =
				svcStudentEnrollment.getAvailableCoursesByStudent(id);
		
		if (search != null) {
			if(!search.equals("")) {
				availableCourses = availableCourses.stream()
						.filter(
								course ->
									course.getCourseName().toLowerCase().contains(search.toLowerCase())
									|| course.getDescription().toLowerCase().contains(search.toLowerCase()))
						.toList();
			}
		}
		
		model.addAttribute("courseList", availableCourses);
		model.addAttribute("status", "available");
		return "course-gallery";
	}
	
	@GetMapping("/course/enrolled")
	public String getEnrolledCourses(
			@PathVariable Long id,
			@RequestParam(value = "search", required = false) String search,
			Model model) {
		List<StudentEnrollment> enrolledCourses =
				svcStudentEnrollment.getEnrolledCoursesByStudent(id);
		
		if (search != null) {
			if(!search.equals("")) {
				enrolledCourses = enrolledCourses.stream()
						.filter(
								enrollment ->
									enrollment.getCourse().getCourseName().toLowerCase().contains(search.toLowerCase())
									|| enrollment.getCourse().getDescription().toLowerCase().contains(search.toLowerCase()))
						.toList();
			}
		}
		
		model.addAttribute("enrollmentList", enrolledCourses);
		model.addAttribute("status", "enrolled");
		return "course-enrolled-gallery";
	}
	
	@GetMapping("/course/{courseId}")
	public String getCourseDetail(
			@PathVariable Long courseId,
			Model model) {
		
		Course course = svcCourse.getCourseById(courseId);
		LocalDate today = LocalDate.now();
		
		model.addAttribute("course", course);
		model.addAttribute("today", today);
		return "course-detail";
	}
	
	@GetMapping("/course/enrolled/{courseId}")
	public String getEnrolledCourseDetail(
			@PathVariable("id") Long studentId,
			@PathVariable Long courseId,
			Model model) {

		StudentEnrollment enrollment = svcStudentEnrollment.getEnrollmentByCourseAndStudent(courseId, studentId);
		LocalDate today = LocalDate.now();
		
		model.addAttribute("enrollment", enrollment);
		model.addAttribute("today", today);
		model.addAttribute("status", "enrolled");
		return "course-enrolled-detail";
	}
	
	@PostMapping("/course/{courseId}/enroll")
	public String postCourseEnroll(
			@PathVariable Long courseId,
			@PathVariable("id") Long studentId,
			HttpServletRequest request) {
		
		svcStudentEnrollment.saveStudentEnrollment(courseId, studentId);
		
		Long vacancy = svcCourse.getCourseById(courseId).getCourseVacancy();
		if (vacancy == 1) {
			svcCourse.updateCourseStatus(courseId, 3);
		}
		return String.format("redirect:/student/%d/course/enrolled", studentId);
	}
	
	@GetMapping("/grades")
	public String getStudentGrades(@PathVariable Long id, Model model) {
		List<StudentGrade> grades =
				svcStudentGrade.getStudentGradesByStudent(id).stream()
				.map(grade -> {
					grade.setCourse(svcCourse.getDetailedCourse(grade.getCourse()));
					return grade;
				}).toList();
		model.addAttribute("gradeList", grades);
		return "student-grade-list";
	}
	
	/*** CRUD Operations ***/
	public String getCreateStudent(Model model) {
		model.addAttribute("student", new Student());
		model.addAttribute("action", "create");
		return "student-upsert";
	}

	public String postCreateStudent(Student student, String redirectURI) {
		svcStudent.createStudent(student);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getUpdateStudent(Long id, Model model) {
		Student existingStudent = svcStudent.getStudentById(id);
		existingStudent.setPassword(null);
		model.addAttribute("student", existingStudent);
		model.addAttribute("action", "update");
		return "student-upsert";
	}
	
	public String putUpdateStudent(Long id, Student student, String redirectURI) {
		svcStudent.updateStudent(id, student);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String deleteStudent(Long id, String redirectURI) {
		svcStudent.deleteStudent(id);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getAllStudents(Model model) {
		List<Student> allStudents = svcStudent.getAllStudents();
		model.addAttribute("studentList", allStudents);
		return "student-list";
	}
}
