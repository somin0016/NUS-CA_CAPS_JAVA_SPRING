package team7.services;

import team7.models.Admin;

public interface AdminService {

	Admin getAdminById(Long id);
	
	Admin getAdminByUsername(String username);	
}
