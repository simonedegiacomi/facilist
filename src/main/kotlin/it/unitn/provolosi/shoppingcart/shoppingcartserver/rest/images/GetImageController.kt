package it.unitn.provolosi.shoppingcart.shoppingcartserver.rest.images

import it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images.ImagesService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/images")
class GetImageController (
        private val images: ImagesService
) {

    @GetMapping("/{id}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun downloadImage(@PathVariable id: String, @RequestHeader headers: HttpHeaders?) =
            images.getImageAsResponse(id, headers!!)

}