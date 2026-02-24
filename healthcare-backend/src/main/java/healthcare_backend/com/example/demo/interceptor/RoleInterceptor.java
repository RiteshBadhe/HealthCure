package healthcare_backend.com.example.demo.interceptor;

import healthcare_backend.com.example.demo.annotation.RequireRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Allow OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Check if handler is a method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        // If no @RequireRole annotation, allow access
        if (requireRole == null) {
            return true;
        }

        // Get role from request header (sent by frontend)
        String userRole = request.getHeader("X-User-Role");
        
        // If no role header, deny access
        if (userRole == null || userRole.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized - No role provided\"}");
            return false;
        }

        // Check if user's role is in the allowed roles
        String[] allowedRoles = requireRole.value();
        if (!Arrays.asList(allowedRoles).contains(userRole)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Forbidden - Insufficient permissions\"}");
            return false;
        }

        return true;
    }
}
