package renetik.spring.sample

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Suppress("DEPRECATION")
@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Bean
    public override fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(User.withDefaultPasswordEncoder()
                .username("username")
                .password("password")
                .roles("USER")
                .build())
    }
}