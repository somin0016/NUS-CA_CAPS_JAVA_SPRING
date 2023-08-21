package team7.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opencsv.CSVWriter;

import team7.models.Admin;
import team7.models.Course;
import team7.models.Lecturer;
import team7.models.Student;
import team7.models.StudentEnrollment;
import team7.models.UpdateRequest;
import team7.services.AdminService;
import team7.services.CourseService;
import team7.services.EmailService;
import team7.services.LecturerService;
import team7.services.StudentService;
import team7.services.StudentEnrollmentService;
import team7.services.UpdateRequestService;

@Controller
@RequestMapping("/admin/{id}")
public class AdminController {
	
	/*** Dependency Injections ***/
	@Autowired
	AdminService svcAdmin;
	
	@Autowired
	CourseService svcCourse;
	
	@Autowired
	StudentEnrollmentService svcStudentEnrollment;
	
	@Autowired
	UpdateRequestService svcUpdateRequest;
	
	@Autowired
	EmailService svcEmail;
	
	@Autowired
	LecturerService svcLecturer;

	@Autowired
	StudentService svcStudent;
	
	@Autowired
	CourseController ctrlCourse;
	
	@Autowired
	LecturerController ctrlLecturer;
	
	@Autowired
	StudentController ctrlStudent;

	/*** Methods ***/
	
	/*** ADMIN ***/
	@GetMapping("")
	public String getAdminDashboard(@PathVariable Long id, Model model) {
		Admin existingAdmin = svcAdmin.getAdminById(id);

		List<UpdateRequest> updateRequests = svcUpdateRequest.getAllUpdateRequests().stream()
											.sorted(Comparator.comparing(UpdateRequest::getId).reversed())
											.limit(5)
											.toList();
		
		List<Course> courses = svcCourse.getAllCourses().stream()
								.filter(course -> course.getCourseStatus() != 4)
								.sorted(Comparator.comparing(Course::getCourseStartDate).reversed())
								.limit(5)
								.toList();

		List<StudentEnrollment> enrollments = svcStudentEnrollment.getAllEnrollments().stream()
								.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
								.sorted(Comparator.comparing(StudentEnrollment::getEnrollmentDate).reversed())
								.limit(5)
								.toList();
		
		Integer totalCourses = Math.toIntExact(svcCourse.getAllCourses().stream()
								.filter(course -> course.getCourseStatus() != 4)
								.count());
		Integer totalLecturers = svcLecturer.getAllLecturers().size();
		Integer totalStudents = svcStudent.getAllStudents().size();
		Integer totalEnrollments = Math.toIntExact(svcStudentEnrollment.getAllEnrollments().stream()
									.filter(studentEnrollment -> studentEnrollment.getEnrollmentStatus() == 0 || studentEnrollment.getEnrollmentStatus() == 1 )
									.count());

		model.addAttribute("admin", existingAdmin);
		model.addAttribute("updateRequests", updateRequests);
		model.addAttribute("courses", courses);
		model.addAttribute("enrollments", enrollments);
		model.addAttribute("totalCourses", totalCourses);
		model.addAttribute("totalLecturers", totalLecturers);
		model.addAttribute("totalStudents", totalStudents);
		model.addAttribute("totalEnrollments", totalEnrollments);
		return "dashboard";
	}
	
	/*** COURSE ***/
	@GetMapping("/course")
	public String getAllCourses(Model model) {
		return ctrlCourse.getAllCourses(model);
	}
	
	@GetMapping("/course/create")
	public String getCreateCourse(Model model) {
		return ctrlCourse.getCreateCourse(model);
	}
	
	@PostMapping("/course/create")
	public String postCreateCourse(
			@PathVariable("id") Long adminId,
			@Valid @ModelAttribute("course") Course course,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			List<Lecturer> lecturers = svcLecturer.getAllLecturers();
			model.addAttribute("lecturerList", lecturers);
			model.addAttribute("action", "create");
			return "course-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlCourse.postCreateCourse(course, redirectURI);
	}
	
	@GetMapping("/course/update/{courseId}")
	public String getUpdateCourse(@PathVariable Long courseId, Model model) {
		return ctrlCourse.getUpdateCourse(courseId, model);
	}
	
	@PutMapping("/course/update/{courseId}")
	public String putUpdateCourse(
			@PathVariable Long courseId,
			@Valid @ModelAttribute("course") Course course,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			List<Lecturer> lecturers = svcLecturer.getAllLecturers();
			model.addAttribute("lecturerList", lecturers);
			model.addAttribute("action", "update");
			return "course-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlCourse.putUpdateCourse(courseId, course, redirectURI);
	}
	
	@GetMapping("/course/delete/{courseId}")
	public String deleteCourse(
			@PathVariable Long courseId,
			HttpServletRequest request) {
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlCourse.deleteCourse(courseId, redirectURI);
	}
	
	@GetMapping("/course/{courseId}/student")
	public String getStudentsByCourse(
			@PathVariable Long courseId,
			Model model) {
		return ctrlCourse.getStudentsInCourse(courseId, model);
	}
	
	@GetMapping("/course/{courseId}/student/{studentId}/{status}")
	public String updateEnrollmentStatusInCourse(
			@PathVariable("id") Long adminId,
			@PathVariable Long courseId,
			@PathVariable Long studentId,
			@PathVariable Integer status) {
		
		String redirectURI = String.format("/admin/%d/enrollment", adminId);
		return this.updateEnrollmentStatus(courseId, studentId, status, redirectURI);
	}
		
	public String updateEnrollmentStatus(
			Long courseId, Long studentId,
			Integer status, String redirectURI) {
		
		svcStudentEnrollment.updateEnrollmentStatus(courseId, studentId, status);
		
		StudentEnrollment enrollment =
				svcStudentEnrollment.getEnrollmentByCourseAndStudent(courseId, studentId);
		Student student = enrollment.getStudent();
		Course course = svcCourse.getDetailedCourse(enrollment.getCourse());
		
		String recipient = enrollment.getStudent().getEmail();
		String subject = "";
		String template = "";
		
		if (status == 1) {			
			subject = String.format("Enrollment Accepted (Course: %s)", course.getCourseName());
			template =
					"Congratulations! " + student.getFirstName() + ' ' + student.getLastName() + ".\n" +
							"Your enrollment for " + course.getCourseName() + " enrolled on " + ' ' + enrollment.getEnrollmentDate() +
							" has been successfully accepted.\n" + 
							"Course Start Date: " + course.getCourseStartDate() + " \n" + 
							"Course End Date: " + course.getCourseEndDate() +
							"Course Duration: " + course.getCourseDuration();
		}
		else if (status == 2) {			
			subject = String.format("Enrollment Rejected (Course: %s)", course.getCourseName());
			template =
					"We are so sorry, " + student.getFirstName() + ' ' + student.getLastName() + ".\n" +
							"Your enrollment for " + course.getCourseName() + " enrolled on " + ' ' + enrollment.getEnrollmentDate() +
							" has been rejected.\n";
		}
		
		svcEmail.sendEmail(recipient, subject, template);
		return String.format("redirect:%s", redirectURI);
	}
	
	
	/*** LECTURER ***/
	@GetMapping("/lecturer")
	public String getAllLecturers(Model model) {
		return ctrlLecturer.getAllLecturers(model);
	}
	
	@GetMapping("/lecturer/create")
	public String getCreateLecturer(Model model) {
		return ctrlLecturer.getCreateLecturer(model);
	}
	
	@PostMapping("/lecturer/create")
	public String postCreateLecturer(
		@Valid @ModelAttribute("lecturer") Lecturer lecturer,
		BindingResult bindingResult,
		HttpServletRequest request,
		Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "create");
			return "lecturer-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlLecturer.postCreateLecturer(lecturer, redirectURI);
	}
	
	@GetMapping("/lecturer/update/{lecturerId}")
	private String getUpdateLecturer(
			@PathVariable Long lecturerId,
			Model model) {
		return ctrlLecturer.getUpdateLecturer(lecturerId, model);
	}
	
	@PutMapping("/lecturer/update/{lecturerId}")
	private String putUpdateLecturer(
			@PathVariable Long lecturerId,
			@Valid @ModelAttribute("lecturer") Lecturer lecturer,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "update");
			return "lecturer-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlLecturer.putUpdateLecturer(lecturerId, lecturer, redirectURI);
	}
	
	@GetMapping("/lecturer/delete/{lecturerId}")
	private String deleteLecturer(
			@PathVariable Long lecturerId,
			HttpServletRequest request) {
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlLecturer.deleteLecturer(lecturerId, redirectURI);
	}
	
	/*** STUDENT ***/
	@GetMapping("/student")
	public String getAllStudents(Model model) {
		return ctrlStudent.getAllStudents(model);
	}
	
	@GetMapping("/student/create")
	public String getCreateStudent(
			Model model) {
		return ctrlStudent.getCreateStudent(model);
	}
	
	@PostMapping("/student/create")
	public String postCreateStudent(
			@Valid @ModelAttribute("student") Student student,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "create");
			return "student-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlStudent.postCreateStudent(student, redirectURI);
	}
	
	@GetMapping("/student/update/{studentId}")
	public String getUpdateStudent(
			@PathVariable Long studentId,
			Model model) {
		return ctrlStudent.getUpdateStudent(studentId, model);
	}
	
	@PutMapping("/student/update/{studentId}")
	public String putUpdateStudent(
			@PathVariable Long studentId,
			@Valid @ModelAttribute("student") Student student,
			BindingResult bindingResult,
			HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "update");
			return "student-upsert";
		}
		
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlStudent.putUpdateStudent(studentId, student, redirectURI);
	}
	
	@GetMapping("/student/delete/{studentId}")
	public String deleteStudent(
			@PathVariable Long studentId,
			HttpServletRequest request) {
		String URI = request.getRequestURI();
		List<String> splitURI = new ArrayList<String>(Arrays.asList(URI.split("/")));
		splitURI.remove(splitURI.size() - 1);
		splitURI.remove(splitURI.size() - 1);
		String redirectURI = String.join("/", splitURI);
		
		return ctrlStudent.deleteStudent(studentId, redirectURI);
	}
	
	/*** ENROLLMENTS ***/
	@GetMapping("/enrollment")
	public String getAllEnrollments(Model model) {
		List<StudentEnrollment> enrollments = svcStudentEnrollment.getAllEnrollments();
		model.addAttribute("enrollmentList", enrollments);
		return "admin-student-enrollment-list";
	}
	
	/*** UPDATE REQUESTS ***/
	@GetMapping("/request")
	public String getAllUpdateRequests(Model model) {
		List<UpdateRequest> updateRequests = svcUpdateRequest.getAllUpdateRequests();
		model.addAttribute("requestList", updateRequests);
		
		return "admin-update-request-list";
	}
	
	
	@GetMapping("/request/{requestId}/{status}")
	public String getProcessUpdateRequest(
			@PathVariable("id") Long adminId,
			@PathVariable Long requestId,
			@PathVariable Integer status) {
		
		svcUpdateRequest.changeUpdateRequestStatus(requestId, status);
		return String.format("redirect:/admin/%d/request", adminId);
	}

	@GetMapping("/export/{type}")
    public void exportToCSV(HttpServletResponse response, @PathVariable("type") String reportType) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
		String csvFileName;

        if (reportType.equals("courses")) {
			csvFileName = "AllCourses.csv";

			// Set the response content type for CSV
        	response.setContentType("text/csv");

			// Set the response headers for file download
        	response.setHeader("Content-Disposition", "attachment; filename=" + currentDateTime + csvFileName);

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
				// Write the header row
				String[] header = { "Course ID", "Lecturer", "Course Name", "Description", "Credits", "Capacity", "Fees", "Start Date", "Duration", "Status", "Number of Enrollments" };
				writer.writeNext(header);

				// Get the list of all courses
				List<Course> allCourses = svcCourse.getAllCourses().stream()
											.sorted(Comparator.comparingInt(course -> course.getEnrollment().size()))
											.collect(Collectors.toList());
				Collections.reverse(allCourses);

				// Write the data rows
				for (Course course : allCourses) {
					String status;
					if (course.getCourseStatus() == 1 ) {
						status = "Available";
					} else if (course.getCourseStatus() == 2 ) {
						status = "Low Vacancy";
					} else if (course.getCourseStatus() == 3 ) {
						status = "Full";
					} else {
						status = "Cancelled";
					}
					Integer count = course.getEnrollment().size();
					String[] rowData = {
						String.valueOf(course.getId()),
						course.getLecturer().getCode(),
						course.getCourseName(),
						course.getDescription(),
						String.valueOf(course.getCourseCredits()),
						String.valueOf(course.getCourseCapacity()),
						String.valueOf(course.getCourseFee()),
						String.valueOf(course.getCourseStartDate()),
						String.valueOf(course.getCourseDuration()),
						status,
						String.valueOf(count)

					};
					writer.writeNext(rowData);
				}
			} catch (IOException e) {
				// Handle the exception appropriately
				e.printStackTrace();
			}
        } else if (reportType.equals("students")) {
			csvFileName = "AllStudents.csv";

			// Set the response content type for CSV
        	response.setContentType("text/csv");

			// Set the response headers for file download
        	response.setHeader("Content-Disposition", "attachment; filename=" + currentDateTime + csvFileName);

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
				// Write the header row
				String[] header = { "Student ID", "Matric Number", "Name", "Gender", "Date of Birth", "Email", "Phone" };
				writer.writeNext(header);

				// Get the list of all courses
				List<Student> allStudents = svcStudent.getAllStudents();

				// Write the data rows
				for (Student student : allStudents) {
					String[] rowData = {
						String.valueOf(student.getId()),
						student.getMatricNumber(),
						student.getFirstName() + " " + student.getLastName(),
						student.getGender(),
						String.valueOf(student.getDob()),
						student.getEmail(),
						student.getPhone()

					};
					writer.writeNext(rowData);
				}
			} catch (IOException e) {
				// Handle the exception appropriately
				e.printStackTrace();
			}
        } else if (reportType.equals("lecturers")) {
			csvFileName = "AllLecturers.csv";

			// Set the response content type for CSV
        	response.setContentType("text/csv");

			// Set the response headers for file download
        	response.setHeader("Content-Disposition", "attachment; filename=" + currentDateTime + csvFileName);

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
				// Write the header row
				String[] header = { "Enrolment Id", "Course", "Student ID", "Matric Number", "Student Name", "Gender", "Date of Birth", "Email", "Phone", "Enrollment Status"};
				writer.writeNext(header);

				// Get the list of all courses
				List<Lecturer> allLecturers = svcLecturer.getAllLecturers();

				// Write the data rows
				for (Lecturer lecturer : allLecturers) {
					String title;
					if (lecturer.getTitle() == 0 ) {
						title = "Mr. ";
					} else if (lecturer.getTitle() == 1 ) {
						title = "Mrs. ";
					} else {
						title = "Ms. ";
					}
					String[] rowData = {
						String.valueOf(lecturer.getId()),
						lecturer.getCode(),
						title + lecturer.getFirstName() + " " + lecturer.getLastName(),
						lecturer.getDesignation(),
						lecturer.getEmail(),
						lecturer.getPhone()

					};
					writer.writeNext(rowData);
				}
			} catch (IOException e) {
				// Handle the exception appropriately
				e.printStackTrace();
			}

        } else if (reportType.equals("enrollments")) {
			csvFileName = "AllEnrollments.csv";

			// Set the response content type for CSV
        	response.setContentType("text/csv");

			// Set the response headers for file download
        	response.setHeader("Content-Disposition", "attachment; filename=" + currentDateTime + csvFileName);

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
				// Write the header row
				String[] header = { "Enrolment Id", "Course", "Student ID", "Matric Number", "Student Name", "Gender", "Date of Birth", "Email", "Phone", "Enrollment Status"};
				writer.writeNext(header);

					// Get the list of all courses
				List<StudentEnrollment> enrolledStudents = svcStudentEnrollment.getAllEnrollments();

				for (StudentEnrollment studentEnrollment : enrolledStudents) {
					String status;
					if (studentEnrollment.getEnrollmentStatus() == 0 ) {
						status = "Pending";
					} else if (studentEnrollment.getEnrollmentStatus() == 1 ) {
						status = "Accepted";
					} else if (studentEnrollment.getEnrollmentStatus() == 2 ) {
						status = "Rejected";
					} else {
						status = "Cancelled";
					}

					String[] rowData = {
						String.valueOf(studentEnrollment.getId()),
						studentEnrollment.getCourse().getCourseName(),
						String.valueOf(studentEnrollment.getStudent().getId()),
						String.valueOf(studentEnrollment.getStudent().getMatricNumber()),
						studentEnrollment.getStudent().getFirstName() + " " + studentEnrollment.getStudent().getLastName(),
						studentEnrollment.getStudent().getGender(),
						String.valueOf(studentEnrollment.getStudent().getDob()),
						studentEnrollment.getStudent().getEmail(),
						studentEnrollment.getStudent().getPhone(),
						status
					};
					writer.writeNext(rowData);
				}
			} catch (IOException e) {
				// Handle the exception appropriately
				e.printStackTrace();
			}
		}
    }

	@GetMapping("/course/{courseId}/student/export")
    public void exportEnrolledStudents(HttpServletResponse response,@PathVariable("courseId") Long courseId) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
		String csvFileName = "EnrolledStudents.csv";

		// Set the response content type for CSV
		response.setContentType("text/csv");

		// Set the response headers for file download
		response.setHeader("Content-Disposition", "attachment; filename=" + currentDateTime + csvFileName);

		try (CSVWriter writer = new CSVWriter(response.getWriter())) {
			// Write the header row
			String[] header = { "Enrolment Id", "Course", "Student ID", "Matric Number", "Student Name", "Gender", "Date of Birth", "Email", "Phone", "Enrollment Status"};
			writer.writeNext(header);

			// Get the list of all courses
			List<StudentEnrollment> enrolledStudents = svcStudentEnrollment.getStudentEnrollmentsByCourse(courseId);
			for (StudentEnrollment studentEnrollment : enrolledStudents) {
				String status;
				if (studentEnrollment.getEnrollmentStatus() == 0 ) {
					status = "Pending";
				} else if (studentEnrollment.getEnrollmentStatus() == 1 ) {
					status = "Accepted";
				} else if (studentEnrollment.getEnrollmentStatus() == 2 ) {
					status = "Rejected";
				} else {
					status = "Cancelled";
				}

				String[] rowData = {
					String.valueOf(studentEnrollment.getId()),
					studentEnrollment.getCourse().getCourseName(),
					String.valueOf(studentEnrollment.getStudent().getId()),
					String.valueOf(studentEnrollment.getStudent().getMatricNumber()),
					studentEnrollment.getStudent().getFirstName() + " " + studentEnrollment.getStudent().getLastName(),
					studentEnrollment.getStudent().getGender(),
					String.valueOf(studentEnrollment.getStudent().getDob()),
					studentEnrollment.getStudent().getEmail(),
					studentEnrollment.getStudent().getPhone(),
					status
				};
				writer.writeNext(rowData);
			}
		} catch (IOException e) {
			// Handle the exception appropriately
			e.printStackTrace();
		}
	}
	
}
