import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


fun <T> notFound(): ResponseEntity<T> = ResponseEntity.notFound().build()

fun <T> forbidden(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.FORBIDDEN).build()

fun <T> ok(): ResponseEntity<T> = ResponseEntity.ok().build()

fun <T> ok(entity: T): ResponseEntity<T> = ResponseEntity.ok(entity)

fun <T> badRequest(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

fun <T> conflict(): ResponseEntity<T> = ResponseEntity.status(HttpStatus.CONFLICT).build()

fun <T> created(entity: T): ResponseEntity<T> = ResponseEntity(entity, HttpStatus.CREATED)