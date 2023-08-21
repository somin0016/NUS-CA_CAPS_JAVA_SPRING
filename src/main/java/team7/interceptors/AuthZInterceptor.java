package team7.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*** Authorization Interceptor ***/
/*** Blacklisting; Default Allow All ***/
@Component
public class AuthZInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws IOException {
		
		HttpSession session = request.getSession(); 
		
		String URI = request.getRequestURI();
		String[] splitURI = URI.split("/");
		System.out.println(String.format("Requesting URI: %s", URI));

		if (URI.startsWith("/lecturer")
				&& (!session.getAttribute("role").equals(splitURI[1])
				|| !session.getAttribute("userId").toString().equals(splitURI[2]))) {
			response.sendRedirect("/error/403");
		}
		
		if (URI.startsWith("/student") && !session.getAttribute("role").equals("student")) {
			response.sendRedirect("/error/403");
		}
		
		if (URI.startsWith("/admin")
				&& (!session.getAttribute("role").equals(splitURI[1])
				|| !session.getAttribute("userId").toString().equals(splitURI[2]))) {
			response.sendRedirect("/error/403");
		}
		
		String[] blacklist = new String[] {
			"/courses"
		};
		
		for (String baseUrl: blacklist) {
			if (URI.startsWith(baseUrl)) {
				response.sendRedirect("/error/403");
			}
		}
		
		return true;
	}
	
}
