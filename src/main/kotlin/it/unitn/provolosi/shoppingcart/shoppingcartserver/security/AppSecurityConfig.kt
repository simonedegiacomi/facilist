package it.unitn.provolosi.shoppingcart.shoppingcartserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource

@Configuration()
@EnableWebSecurity()
@EnableGlobalMethodSecurity(
    prePostEnabled  = true,
    jsr250Enabled   = true
)
class AppSecurityConfig(

        @Autowired
        private val dataSource: DataSource

) : WebSecurityConfigurerAdapter() {

    companion object {
        const val QUERY_GET_USER_BY_USERNAME = "SELECT email AS username, password, enabled FROM user where email = ?"
        const val QUERY_GET_ROLES_BY_USERNAME = "SELECT email AS username, role FROM user WHERE email = ?"
    }

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests()
                .anyRequest()
                    .authenticated()

                .and().formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("email")
                    .successHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
                    .failureHandler { _, response, _ -> response.status = HttpStatus.FORBIDDEN.value() }

                .and().rememberMe()
                    .rememberMeParameter("rememberMe")

                .and().csrf().disable()
    }


    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(QUERY_GET_USER_BY_USERNAME)
                .authoritiesByUsernameQuery(QUERY_GET_ROLES_BY_USERNAME)
    }

    @Bean("passwordEncoder")
    fun passwordEncoder() = BCryptPasswordEncoder()
}
