package uz.com.onlineshop.filter;


import jakarta.servlet.http.HttpServletRequest;

public class IpAddressUtil {

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-Forwarded-For");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("Proxy-Client-IP");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_X_FORWARDED");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_CLIENT_IP");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_FORWARDED_FOR");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_FORWARDED");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("HTTP_VIA");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getHeader("REMOTE_ADDR");
            }
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}