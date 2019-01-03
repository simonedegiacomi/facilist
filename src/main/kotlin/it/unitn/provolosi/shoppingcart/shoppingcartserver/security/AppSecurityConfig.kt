package it.unitn.provolosi.shoppingcart.shoppingcartserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.sql.DataSource

/**
 * Configuration to enable the Annotations relative to the security
 */
@Configuration()
@EnableWebSecurity()
@EnableGlobalMethodSecurity(
    prePostEnabled  = true,
    jsr250Enabled   = true
)
class AppSecurityConfig(

        @Autowired
        private val dataSource: DataSource,

        @Value("\${forceHerokuHttps}")
        private val forceHerokuHttps: Boolean

) : WebSecurityConfigurerAdapter() {

    companion object {
        const val QUERY_GET_USER_BY_USERNAME = "SELECT email AS username, password, enabled FROM user where email = ?"
        const val QUERY_GET_ROLES_BY_USERNAME = "SELECT email AS username, role FROM user WHERE email = ?"

        val unauthenticatedApiEndpoints = arrayOf(
            "/api/users/register",
            "/api/users/verifyEmail/*",
            "/api/users/*/recoverPassword",
            "/api/users/completeRecoverPassword",

            "/api/shoppingListCategories",

            "/api/productCategories",

            "/api/products/search",

            "/api/images/*"
        )
    }

    /**
     * Configure the path authentication and authorization
     */
    override fun configure(http: HttpSecurity?) {
        if (forceHerokuHttps) {
            http!!.requiresChannel()
                .requestMatchers(RequestMatcher { r -> r.getHeader("X-Forwarded-Proto") != null })
                .requiresSecure()
        }

        http!!.authorizeRequests()
                .antMatchers("/index.html")
                    .permitAll()

                .antMatchers(*unauthenticatedApiEndpoints)
                    .permitAll()

                .antMatchers("/api/**")
                    .authenticated()

                .and().formLogin()
                    .loginPage("/api/auth/login")
                    .usernameParameter("email")
                    .successHandler { _, response, _ -> response.status = HttpStatus.OK.value() }
                    .failureHandler { _, response, _ -> response.status = HttpStatus.FORBIDDEN.value() }

                .and().exceptionHandling()
                    .accessDeniedHandler { _, response, _ -> response.status = HttpStatus.FORBIDDEN.value() }
                    .authenticationEntryPoint { _, response, authException ->
                        if (authException != null) {
                            response.status = HttpStatus.FORBIDDEN.value()
                        }
                    }

                .and().rememberMe()
                    .rememberMeParameter("rememberMe")

                .and().csrf().disable()
    }

    /**
     * Configure the authentication method to use to convert the email (stored in the session) to the User object
     * used in the application
     */
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(QUERY_GET_USER_BY_USERNAME)
                .authoritiesByUsernameQuery(QUERY_GET_ROLES_BY_USERNAME)
    }
}
