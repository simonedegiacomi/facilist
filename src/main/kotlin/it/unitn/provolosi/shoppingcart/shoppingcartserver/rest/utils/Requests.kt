import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.protocolPortAndDomain(): String {
    val protocolAndDomain = "$scheme://$serverName"
    if (serverPort != 80 || serverPort != 443) {
        return "$protocolAndDomain:$serverPort"
    }
    return protocolAndDomain
}


fun <T> notFound(): ResponseEntity<T> = ResponseEntity.notFound().build()

fun <T> forbidden(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.FORBIDDEN).build()