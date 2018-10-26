package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.auth

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class LogoutController {

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    fun logout(): ResponseEntity<Any> {
        SecurityContextHolder.clearContext()

        return ResponseEntity.ok().build()
    }

}