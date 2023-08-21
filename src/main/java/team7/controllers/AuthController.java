package team7.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import team7.exceptions.ResourceNotFoundException;
import team7.exceptions.UnauthorizedException;
import team7.models.Admin;
import team7.models.Lecturer;
import team7.models.Student;
import team7.services.AdminService;
import team7.services.LecturerService;
import team7.services.StudentService;

@Controller
public class AuthController {
	
	@Autowired
	StudentService svcStudent;
	
	@Autowired
	LecturerService svcLecturer;
	
	@Autowired
	AdminService svcAdmin;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("/login/{role}")
	public String login(@PathVariable String role,Model model, HttpSession session) {
		model.addAttribute("role", role);
		switch(role) {
			case "student":
				model.addAttribute("student", new Student());
			case "lecturer":
				model.addAttribute("lecturer", new Lecturer());
			case "admin":
				model.addAttribute("admin", new Admin());
		}
		
		return "login";
	}
	
	@PostMapping("/login/student")
	public String authStudent(
			@ModelAttribute("student") Student student,
			Model model,
			HttpSession session) {
		
		try {
			System.out.println(student.getEmail());
			System.out.println(student.getPassword());
			Student existingStudent = svcStudent.getStudentByEmail(student.getEmail());
			System.out.println(existingStudent.getEmail());
			if (passwordEncoder.matches(student.getPassword(), existingStudent.getPassword())) {
				session.setAttribute("role", "student");
				session.setAttribute("userId", existingStudent.getId());
				session.setAttribute(
						"username",
						String.format("%s %s",
								existingStudent.getFirstName(),
								existingStudent.getLastName()));
				return String.format(
						"redirect:/%s/%s",
						session.getAttribute("role"),
						session.getAttribute("userId"));
			}
			else {
				throw new UnauthorizedException();
			}
		}
		catch (ResourceNotFoundException e) {
			return "redirect:/login/student";
		}
		catch (Exception e) {
			return "redirect:/login/student";
		}
	}
	
	@PostMapping("/login/lecturer")
	public String authLecturer(
			@ModelAttribute("lecturer") Lecturer lecturer,
			Model model,
			HttpSession session) {
		
		try {
			Lecturer existingLecturer = svcLecturer.getLecturerByEmail(lecturer.getEmail());
			
			if (passwordEncoder.matches(lecturer.getPassword(), existingLecturer.getPassword())) {
				session.setAttribute("role", "lecturer");
				session.setAttribute("userId", existingLecturer.getId());
				session.setAttribute(
						"username",
						String.format(
								"%s %s",
								existingLecturer.getFirstName(),
								existingLecturer.getLastName()));
				return String.format(
						"redirect:/%s/%s",
						session.getAttribute("role"),
						session.getAttribute("userId"));
			}
			else {
				throw new UnauthorizedException();
			}
		}
		catch (ResourceNotFoundException e) {
			return "redirect:/login/lecturer";
		}
		catch (Exception e) {
			return "redirect:/login/lecturer";
		}
	}
	
	@PostMapping("/login/admin")
	public String authAdmin(
			@ModelAttribute("admin") Admin admin,
			Model model,
			HttpSession session) {
		
		try {
			Admin existingAdmin = svcAdmin.getAdminByUsername(admin.getUsername());
			System.out.println(existingAdmin.getUsername());
			if (passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
				session.setAttribute("role", "admin");
				session.setAttribute("userId", existingAdmin.getId());
				session.setAttribute(
						"username",
						String.format(
								"%s %s",
								existingAdmin.getFirstName(),
								existingAdmin.getLastName()));
				return String.format(
						"redirect:/%s/%s",
						session.getAttribute("role"),
						session.getAttribute("userId"));
			}
			else {
				throw new UnauthorizedException();
			}
		}
		catch (ResourceNotFoundException e) {
			return "redirect:/login/admin";
		}
		catch (Exception e) {
			return "redirect:/login/admin";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
