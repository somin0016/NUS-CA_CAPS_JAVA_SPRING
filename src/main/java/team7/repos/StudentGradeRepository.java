package team7.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team7.models.StudentGrade;

@Repository
public interface StudentGradeRepository extends JpaRepository<StudentGrade, Long> {

	List<StudentGrade> findByStudentId(Long studentId);
	
	List<StudentGrade> findByCourseId(Long courseId);
	
	Optional<StudentGrade> findByCourseIdAndStudentId(Long courseId, Long studentId);
}
