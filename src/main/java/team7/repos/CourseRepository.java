package team7.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team7.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

}
