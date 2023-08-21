package team7.services;

import java.util.List;

import team7.models.UpdateRequest;

public interface UpdateRequestService {

	UpdateRequest createUpdateRequest(UpdateRequest request);
	
	UpdateRequest updateUpdateRequest(Long id, UpdateRequest request);
	
	Boolean deleteUpdateRequest(Long id);
	
	List<UpdateRequest> getAllUpdateRequests();
	
	UpdateRequest getUpdateRequestById(Long requestId);
	
	List<UpdateRequest> getUpdateRequestsByLecturer(Long lecturerId);
	
	Boolean changeUpdateRequestStatus(Long requestId, Integer status);
}
