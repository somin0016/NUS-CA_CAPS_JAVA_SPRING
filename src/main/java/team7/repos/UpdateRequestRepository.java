package team7.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team7.models.UpdateRequest;

@Repository
public interface UpdateRequestRepository extends JpaRepository<UpdateRequest, Long> {
	
	List<UpdateRequest> findByLecturerId(Long lecturerId);
}
