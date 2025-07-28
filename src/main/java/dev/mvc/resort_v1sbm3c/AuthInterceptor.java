package dev.mvc.resort_v1sbm3c;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 세션에 저장된 로그인 사용자 정보 (예: "loginUser") 확인
        Object memberno = request.getSession().getAttribute("memberno");
        if (memberno == null) {
            // 로그인 안 된 상태면 로그인 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/member/login_cookie_need");
            return false;  // 이후 controller 호출 차단
        }
        return true;  // 로그인 상태면 정상 진행
    }
}

