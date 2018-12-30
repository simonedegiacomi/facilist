package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.images

import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images.ImagesService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream

@RestController
@RequestMapping("/api/images")
class UploadImageController (
        private val images: ImagesService
) {

    @PostMapping(consumes = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE])
    fun uploadImage(fromClient: InputStream) = images.storeImage(fromClient)
}
