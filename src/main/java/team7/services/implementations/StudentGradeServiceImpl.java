package team7.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Course;
import team7.models.StudentGrade;
import team7.repos.LecturerRepository;
import team7.repos.StudentGradeRepository;
import team7.services.StudentGradeService;

@Service
public class StudentGradeServiceImpl implements StudentGradeService {
	
	@Autowired
	StudentGradeRepository repoStudentGrade;
	
	@Autowired
	LecturerRepository repoLecturer;

	public StudentGrade getStudentGrade(Long courseId, Long studentId) {
		
		return repoStudentGrade.findByCourseIdAndStudentId(courseId, studentId)
				.orElse(new StudentGrade());
	}
	
	public List<StudentGrade> getStudentGradesByStudent(Long studentId){
		return repoStudentGrade.findByStudentId(studentId);			
	}
	
	public List<StudentGrade> getStudentGradesByLecturer(Long lecturerId) {
		List<Course> lecturerCourses =
				repoLecturer.findById(lecturerId)
				.map(lecturer -> {
					return lecturer.getCourses();
				}).orElseThrow(() -> new ResourceNotFoundException());
		
		List<StudentGrade> allStudentGrades = new ArrayList<StudentGrade>();
		
		for (Course course: lecturerCourses) {
			allStudentGrades.addAll(repoStudentGrade.findByCourseId(course.getId()));
		}
		
		return allStudentGrades;
	}
	
	public StudentGrade createStudentGrade(StudentGrade grade) {
		return repoStudentGrade.save(grade);
	}
	
	public StudentGrade updateStudentGrade(Long courseId, Long studentId, StudentGrade grade) {
		return repoStudentGrade.findByCourseIdAndStudentId(courseId, studentId)
				.map(existingGrade -> {
					existingGrade.setStudentGrade(grade.getStudentGrade());
					existingGrade.setStudentCredits(grade.getStudentCredits());
					
					return repoStudentGrade.save(existingGrade);
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
