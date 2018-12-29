import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest


fun <T> notFound(): ResponseEntity<T> = ResponseEntity.notFound().build()

fun <T> forbidden(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.FORBIDDEN).build()

fun <T> ok(): ResponseEntity<T> = ResponseEntity.ok().build()

fun <T> badRequest(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
