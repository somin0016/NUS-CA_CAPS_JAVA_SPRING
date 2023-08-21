package team7.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team7.models.Lecturer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

	Optional<Lecturer> findByEmail(String email);
}
