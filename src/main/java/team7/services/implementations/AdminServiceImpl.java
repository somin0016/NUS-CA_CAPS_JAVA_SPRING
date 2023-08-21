package team7.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import team7.exceptions.ResourceNotFoundException;
import team7.models.Admin;
import team7.repos.AdminRepository;
import team7.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository repoAdmin;
	
	public Admin getAdminById(Long id) {
		return repoAdmin.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Admin getAdminByUsername(String username) {
		return repoAdmin.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
