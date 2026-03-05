package ba.etf.faculty.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static String getToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute("token");
    }

    public static String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute("role");
    }

    public static Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Long) session.getAttribute("userId");
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getToken(request) != null;
    }

    public static boolean isFaculty(HttpServletRequest request) {
        return "FACULTY".equals(getRole(request));
    }

    public static void setSession(HttpServletRequest request, String token,
                                  String role, Long userId, String username) {
        HttpSession session = request.getSession(true);
        session.setAttribute("token", token);
        session.setAttribute("role", role);
        session.setAttribute("userId", userId);
        session.setAttribute("username", username);
    }

    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}