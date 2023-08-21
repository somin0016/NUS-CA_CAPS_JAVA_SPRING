package team7.controllers;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import team7.models.Course;
import team7.models.Lecturer;
import team7.models.Student;
import team7.models.StudentEnrollment;
import team7.models.StudentGrade;
import team7.models.UpdateRequest;
import team7.services.CourseService;
import team7.services.LecturerService;
import team7.services.StudentEnrollmentService;
import team7.services.StudentGradeService;
import team7.services.StudentService;
import team7.services.UpdateRequestService;

@Controller
@RequestMapping("/lecturer/{id}")
public class LecturerController {

	/*** Dependency Injections ***/
	@Autowired
	LecturerService svcLecturer;
	
	@Autowired
	CourseService svcCourse;
	
	@Autowired
	StudentService svcStudent;
	
	@Autowired
	StudentGradeService svcStudentGrade;
	
	@Autowired
	StudentEnrollmentService svcStudentEnrollment;
	
	@Autowired
	UpdateRequestService svcUpdateRequest;
	
	@Autowired
	CourseController controlCourse;

	/*** Methods ***/
	/*** Functional Operations ***/
	@GetMapping("")
	public String getLecturerDashboard(@PathVariable Long id, Model model) {
		Lecturer existingLecturer = svcLecturer.getLecturerById(id);
		List<Course> courses = existingLecturer.getCourses().stream()
								.filter(course -> course.getCourseStatus() != 4)
								.sorted(Comparator.comparing(Course::getCourseStartDate).reversed())
								.limit(5)
								.toList();
		List<StudentEnrollment> enrolledStudents = existingLecturer.getCourses().stream()
													.flatMap(course -> course.getEnrollment().stream())
													.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
													.sorted(Comparator.comparing(StudentEnrollment::getEnrollmentDate).reversed())
													.limit(5)
													.toList();

		Integer totalCourses = Math.toIntExact(existingLecturer.getCourses().stream()
								.filter(course -> course.getCourseStatus() != 4)
								.count());
		Integer totalEnrolled = Math.toIntExact(existingLecturer.getCourses().stream()
								.flatMap(course -> course.getEnrollment().stream())
								.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
								.count());

		model.addAttribute("lecturer", existingLecturer);
		model.addAttribute("courses", courses);
		model.addAttribute("enrolledStudents", enrolledStudents);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("totalEnrolled", totalEnrolled);
		return "dashboard";
	}
	
	@GetMapping("/course")
	public String getCoursesByLecturer(@PathVariable Long id, Model model) {
		Lecturer existingLecturer = svcLecturer.getLecturerById(id);
		List<Course> courses =
				existingLecturer.getCourses().stream()
				.map(c -> svcCourse.getDetailedCourse(c)).toList();
		model.addAttribute("courseList", courses);
		return "course-list";
	}
	
	@GetMapping("/course/{courseId}/student")
	public String getStudentsInCourseByLecturer(
			@PathVariable Long id,
			@PathVariable Long courseId,
			Model model) {
		return controlCourse.getStudentsInCourse(courseId, model);
	}
	
	@GetMapping("/course/{courseId}/student/{studentId}/grading")
	public String createStudentGrade(
			@PathVariable Long courseId,
			@PathVariable Long studentId,
			Model model) {
		StudentGrade grade = svcStudentGrade.getStudentGrade(courseId, studentId);
		if (grade.getId() == null) {
			Student student = svcStudent.getStudentById(studentId);
			Course course = svcCourse.getCourseById(courseId);
			model.addAttribute("student", student);
			model.addAttribute("course", course);
			model.addAttribute("grade", new StudentGrade());
			model.addAttribute("action", "create");
		} else {
			model.addAttribute("student", grade.getStudent());
			model.addAttribute("course", grade.getCourse());
			model.addAttribute("grade", grade);
			model.addAttribute("action", "update");
		}
		
		return "grading";
	}
	
	@PostMapping("/course/{courseId}/student/{studentId}/grading/create")
	public String createStudentGrade(
			@PathVariable("id") Long lecturerId,
			@PathVariable Long courseId,
			@PathVariable Long studentId,
			@ModelAttribute("grade") StudentGrade grade) {
        
		svcStudentGrade.createStudentGrade(grade);
		return String.format(
				"redirect:/lecturer/%d/course/%d/student",
				lecturerId, courseId);
	}
	
	@PutMapping("/course/{courseId}/student/{studentId}/grading/update")
	public String updateStudentGrade(
			@PathVariable("id") Long lecturerId,
			@PathVariable Long courseId,
			@PathVariable Long studentId,
			@ModelAttribute("grade") StudentGrade grade) {
        
		svcStudentGrade.updateStudentGrade(courseId, studentId, grade);
		return String.format(
				"redirect:/lecturer/%d/course/%d/student",
				lecturerId, courseId);
	}
	
	@GetMapping("/request")
	public String getAllUpdateRequests(
			@PathVariable("id") Long lecturerId,
			Model model) {
		List<UpdateRequest> updateRequests = svcUpdateRequest.getUpdateRequestsByLecturer(lecturerId);
		model.addAttribute("requestList", updateRequests);
		
		return "lecturer-update-request-list";
	}
	
	@GetMapping("/course/{courseId}/request/create")
	public String getCreateUpdateRequest(
			@PathVariable("id") Long lecturerId,
			@PathVariable Long courseId,
			Model model) {
		Lecturer lecturer = svcLecturer.getLecturerById(lecturerId);
		Course course = svcCourse.getCourseById(courseId);
		
		model.addAttribute("lecturer", lecturer);
		model.addAttribute("course", course);
		model.addAttribute("updateRequest", new UpdateRequest());
		model.addAttribute("action", "create");
		return "lecturer-update-request-upsert";
	}
	
	@PostMapping("/course/{courseId}/request/create")
	public String postCreateUpdateRequest(
			@ModelAttribute("updateRequest") UpdateRequest updateRequest,
			@PathVariable Long id,
			HttpServletRequest request) {
		
		svcUpdateRequest.createUpdateRequest(updateRequest);
				
		return String.format("redirect:/lecturer/%d/request", id);
	}
	
	@GetMapping("/request/update/{requestId}")
	public String getUpdateUpdateRequest(
			@PathVariable("id") Long lecturerId,
			@PathVariable Long requestId,
			Model model) {
		Lecturer lecturer = svcLecturer.getLecturerById(lecturerId);
		UpdateRequest updateRequest = svcUpdateRequest.getUpdateRequestById(requestId);
		
		model.addAttribute("lecturer", lecturer);
		model.addAttribute("course", updateRequest.getCourse());
		model.addAttribute("updateRequest", updateRequest);
		model.addAttribute("action", "update");
		return "lecturer-update-request-upsert";
	}
	
	@PutMapping("/request/update/{requestId}")
	public String putUpdateUpdateRequest(
			@ModelAttribute("updateRequest") UpdateRequest updateRequest,
			@PathVariable Long id,
			@PathVariable Long requestId,
			HttpServletRequest request) {
		
		svcUpdateRequest.updateUpdateRequest(requestId, updateRequest);
		
		return String.format("redirect:/lecturer/%d/request", id);
	}
	
	@GetMapping("/request/delete/{requestId}")
	public String deleteUpdateRequest(
			@PathVariable Long id,
			@PathVariable Long requestId,
			HttpServletRequest request) {
		
		svcUpdateRequest.deleteUpdateRequest(requestId);
		
		return String.format("redirect:/lecturer/%d/request", id);
	}
	
	@GetMapping("/student/perf")
	public String getStudentGradings(
			Model model,
			@PathVariable("id") Long lecturerId) {
		List<StudentGrade> grades = svcStudentGrade.getStudentGradesByLecturer(lecturerId);
		
		model.addAttribute("gradeList", grades);
		return "lecturer-student-performance";
	}
	
	/*** CRUD Operations ***/	
	public String getCreateLecturer(Model model) {
		model.addAttribute("lecturer", new Lecturer());
		model.addAttribute("action", "create");
		return "lecturer-upsert";
	}
	
	public String postCreateLecturer(Lecturer lecturer, String redirectURI) {
		svcLecturer.createLecturer(lecturer);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getUpdateLecturer(Long id, Model model) {
		Lecturer existingLecturer = svcLecturer.getLecturerById(id);
		existingLecturer.setPassword(null);
		model.addAttribute("lecturer", existingLecturer);
		model.addAttribute("action", "update");
		return "lecturer-upsert";
	}

	public String putUpdateLecturer(Long id, Lecturer lecturer, String redirectURI) {
		svcLecturer.updateLecturer(id, lecturer);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String deleteLecturer(Long id, String redirectURI) {
		svcLecturer.deleteLecturer(id);
		return String.format("redirect:%s", redirectURI);
	}
	
	public String getAllLecturers(Model model) {
		List<Lecturer> allLecturers = svcLecturer.getAllLecturers();
		model.addAttribute("lecturerList", allLecturers);
		return "lecturer-list";
	}
}
