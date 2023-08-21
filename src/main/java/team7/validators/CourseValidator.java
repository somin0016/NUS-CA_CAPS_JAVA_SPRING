package team7.validators;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import team7.models.Course;

@Component
public class CourseValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Course.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Course course = (Course) obj;

		if (course.getCourseStartDate() == null) {
			errors.rejectValue("courseStartDate", "", "Course Start Date must not be empty.");
		}
		if (course.getCourseStartDate().compareTo(LocalDate.now()) < 0) {
			errors.rejectValue("courseStartDate", "", "Course Start Date must be in the future.");
		}
	}
}
