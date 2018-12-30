package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.io.InputStream

interface ImagesService {

    /**
     * Store an image on the disk from an input stream. The function takes care of choosing a unique file name, which
     * will be returned.
     *
     * @return The path of the stored file.
     */
    @Throws(FileExceedsMaxSizeException::class, FileIsNotAnImageException::class)
    fun storeImage(inputStream: InputStream): String

    fun getImageAsResponse(id: String, headers: HttpHeaders): ResponseEntity<InputStreamResource>
}

class FileExceedsMaxSizeException (size: Long, max: Long) : Exception("Read $size bytes, max is $max bytes")
class FileIsNotAnImageException : Exception()
