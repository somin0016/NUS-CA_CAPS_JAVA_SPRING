package team7.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.UpdateRequest;
import team7.repos.UpdateRequestRepository;
import team7.services.UpdateRequestService;

@Service
public class UpdateRequestServiceImpl implements UpdateRequestService {
	
	@Autowired
	UpdateRequestRepository repoUpdateRequest;

	public UpdateRequest createUpdateRequest(UpdateRequest request) {
		return repoUpdateRequest.save(request);
	}
	
	public UpdateRequest updateUpdateRequest(Long id, UpdateRequest request) {
		return repoUpdateRequest.findById(id)
				.map(existingRequest -> {
					existingRequest.setReason(request.getReason());
					existingRequest.setStatus(request.getStatus());
					
					return repoUpdateRequest.save(existingRequest);
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Boolean deleteUpdateRequest(Long id) {
		return repoUpdateRequest.findById(id)
				.map(existingRequest -> {
					repoUpdateRequest.delete(existingRequest);
					return true;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<UpdateRequest> getAllUpdateRequests() {
		return repoUpdateRequest.findAll();
	}
	
	public UpdateRequest getUpdateRequestById(Long requestId) {
		return repoUpdateRequest.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public List<UpdateRequest> getUpdateRequestsByLecturer(Long lecturerId) {
		return repoUpdateRequest.findByLecturerId(lecturerId);
	}
	
	public Boolean changeUpdateRequestStatus(Long requestId, Integer status) {
		return repoUpdateRequest.findById(requestId)
				.map(existingRequest -> {
					existingRequest.setStatus(status);
					repoUpdateRequest.save(existingRequest);
					return true;
				})
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
