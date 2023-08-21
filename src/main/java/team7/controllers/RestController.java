package team7.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import team7.models.Course;
import team7.models.Lecturer;
import team7.models.Student;
import team7.services.CourseService;
import team7.services.LecturerService;
import team7.services.StudentEnrollmentService;
import team7.services.StudentService;

@Controller
@RequestMapping("/api")
public class RestController {
	
	@Autowired
	StudentService svcStudent;
	
	@Autowired
	LecturerService svcLecturer;
	
	@Autowired
	CourseService svcCourse;
	
	@Autowired
	StudentEnrollmentService svcStudentEnrollment;

	@GetMapping("/students")
	public ResponseEntity<List<Student>> allStudents(){
		return new ResponseEntity<List<Student>>(svcStudent.getAllStudents(), HttpStatus.OK);
	}
	
	@GetMapping("/lecturers")
	public ResponseEntity<List<Lecturer>> allLecturers(){
		return new ResponseEntity<List<Lecturer>>(svcLecturer.getAllLecturers(), HttpStatus.OK);
	}
	
	@GetMapping("/courses")
	public ResponseEntity<List<Course>> allCourses() {
		return new ResponseEntity<List<Course>>(svcCourse.getAllCourses(), HttpStatus.OK);
	}

	@GetMapping("/aa")
	public List<Student> apiStudent() {
		return svcStudent.getAllStudents();
	}

}
