package team7.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import team7.models.Course;
import team7.models.StudentEnrollment;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {

	List<StudentEnrollment> findByCourseId(Long courseId);
	
	List<StudentEnrollment> findByStudentId(Long studentId);
	
	Optional<StudentEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
	
	@Query("SELECT c FROM Course c WHERE c.id NOT IN " +
			"(SELECT e.course.id FROM StudentEnrollment e WHERE e.student.id = :studentId)")
	List<Course> findCoursesNotInEnrollment(@Param("studentId") Long studentId);
}
