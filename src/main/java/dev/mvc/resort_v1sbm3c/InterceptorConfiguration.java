package dev.mvc.resort_v1sbm3c;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // 인증이 필요한 URL 패턴 지정
                .addPathPatterns("/**/create/**", "/**/update/**", "/**/delete/**")
                // 로그인 회원가입 페이지 등은 인증 예외 처리
                .excludePathPatterns("/member/create",
                    "/login", "/logout", "/css/**", "/js/**", "/images/**");
    }
}


