package team7.services;

import java.util.List;

import team7.models.Course;

public interface CourseService {
	
	Course createCourse(Course course);
	
	Course updateCourse(Long id, Course course);
	
	Boolean softDeleteCourse(Long id);
	
	List<Course> getAllCourses();
	
	Course getCourseById(Long id);
	
	Course getDetailedCourse(Course course);
	
	Course updateCourseStatus(Long id, Integer status);
}
