package com.example.graduation4.config;

import com.example.graduation4.jwt.JwtAuthenticationFilter;
import com.example.graduation4.jwt.JwtTokenProvider;
import com.example.graduation4.member.Authority;
import com.example.graduation4.member.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity  //Spring Security 설정 활성화
@EnableWebMvc
// @AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    // private BasicAuthenticationFilter filter;


    //암호화에 필요한 PasswordEncoder Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:8000", "http://localhost:8000", "http://ec2-3-39-19-70.ap-northeast-2.compute.amazonaws.com:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    // authenticationManager Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/css/**, /static/js/**, *.ico");

        // swagger
        web.ignoring().antMatchers(
                "/swagger-ui/**", "/swagger-resources/**", "/api/user", "swagger/**",
                "/v3/api-docs",  "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**","/swagger/**", "/member/login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //h2 콘솔 사용
                .csrf().disable().headers().frameOptions().disable()
                .and()

                //세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //URL 관리
                .authorizeRequests()
                .antMatchers("/member/login", "/swagger-ui/**", "/member/signup", "/member/idCheck", "/member/reissue").permitAll()
                .anyRequest().authenticated()

                // .antMatchers("/member/logout", "/member/{userId}").hasRole("USER")
                // .antMatchers("/member/admin", "/member/{userId}").hasRole("ROLE_ADMIN")
                //.invalidateHttpSession(true);

                .and()
                // JwtAuthenticationFilter를 먼저 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .httpBasic();

        // http.formLogin()
        //         .loginPage("/member/login");
    }

    /*
    @Override
    // @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("mallery").password(passwordEncoder().encode("graduation2023")).roles("ROLE_ADMIN")
                .and()
                .withUser("min").password(passwordEncoder().encode("noonsong2020")).roles("ROLE_USER");

    }

     */


/*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        Member.builder()
                                .username("admin")
                                .password("123456")
                                .roles(Collections.singletonList("ROLE_ADMIN"))
                                .build()
                );
    }

 */


    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("mallery").password(passwordEncoder().encode("graduation2023")).roles("ADMIN")
                .and()
                .withUser("min").password(passwordEncoder().encode("noonsong2020")).roles("USER");

    }

     */


}
