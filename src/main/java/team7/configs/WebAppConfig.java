package team7.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import team7.interceptors.AuthNInterceptor;
import team7.interceptors.AuthZInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {

	@Autowired
	AuthNInterceptor interceptAuthN;
	
	@Autowired
	AuthZInterceptor interceptAuthZ;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptAuthN).order(1);
		registry.addInterceptor(interceptAuthZ).order(2);
	}
}
