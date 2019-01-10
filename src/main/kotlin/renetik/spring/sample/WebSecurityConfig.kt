package renetik.spring.sample

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
    }
}

// should be changed to
//private val ENCODED_PASSWORD = "$2a$10\$AIUufK8g6EFhBcumRRV2L.AQNz3Bjp7oDQVFiO5JJMBFZQ6x2/R/2"
//
//
//@Throws(Exception::class)
//protected fun configure(auth: AuthenticationManagerBuilder) {
//    auth.inMemoryAuthentication()
//            .passwordEncoder(passwordEncoder())
//            .withUser("user").password(ENCODED_PASSWORD).roles("USER")
//}
//
//
//@Bean
//fun passwordEncoder(): PasswordEncoder {
//    return BCryptPasswordEncoder()
//}