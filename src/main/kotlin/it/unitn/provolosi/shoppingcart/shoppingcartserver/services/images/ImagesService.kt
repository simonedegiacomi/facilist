package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.io.InputStream

interface ImagesService {

    /**
     * Store an photo on the disk from an input stream.
     * The photo will be saved in a folder and with a nmae base on the entity name and the entity id.
     *
     * @return The path of the stored file.
     */
    @Throws(FileExceedsMaxSizeException::class, FileIsNotAnImageException::class)
    fun storeImage(inputStream: InputStream): String

    fun getImageAsResponse(id: String, headers: HttpHeaders): ResponseEntity<InputStreamResource>
}

class FileExceedsMaxSizeException (size: Long, max: Long) : Exception("Read $size bytes, max is $max bytes")
class FileIsNotAnImageException : Exception()
