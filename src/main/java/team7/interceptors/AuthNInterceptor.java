package team7.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*** Authentication Interceptor ***/
@Component
public class AuthNInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws IOException {
		
		HttpSession session = request.getSession();
		
		String URI = request.getRequestURI();
		String[] splitURI = URI.split("/");
		
		
		/*** For REST API ***/
		if (URI.startsWith("/api")) {
			return true;
		}
		
		if (URI.startsWith("/public")) {
			return true;
		}
		
		if (splitURI.length == 0 || URI.startsWith("/login")) {
			if (session.getAttribute("userId") == null) {
				return true;
			}
			else {
				response.sendRedirect(String.format("/%s/%s",
						session.getAttribute("role"),
						session.getAttribute("userId")));
			}
		}
		
		if (session.getAttribute("userId") == null && !URI.startsWith("/public")) {
			response.sendRedirect("/");
		}
		else {
			return true;
		}
		
		return false;
	}
}
