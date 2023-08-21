package team7.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {

	@GetMapping(value = {"/", "/home"})
	public String home() {
		return "landing";
	}
	
	@GetMapping("/error/403")
	public String error403() {
		return "403";
	}
}
