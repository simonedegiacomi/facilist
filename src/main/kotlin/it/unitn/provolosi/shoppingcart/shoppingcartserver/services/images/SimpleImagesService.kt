package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO


@Service
class SimpleImagesService(
        @Value("\${uploads.maxSize}")
        private val maxUploadMB: Long,

        @Value("\${uploads.folder}")
        private val uploadsFolderPath: String
) : ImagesService {

    private val maxUploadBytes = megaBytesToBytes(maxUploadMB)

    private val random = Random()

    private fun megaBytesToBytes(MB: Long) = MB * 1024 * 1024

    override fun storeImage(inputStream: InputStream): String {
        val id = UUID.randomUUID().toString()
        val fileName = getFileName(id)
        val tempFileName = getTempFileName(fileName)

        val file = File(uploadsFolderPath, fileName)
        val tempFile = File(uploadsFolderPath, tempFileName)

        // TODO: Convert image if not png
        tempFile.parentFile.mkdir()
        writeToDisk(inputStream, tempFile)
        if (!isImage(tempFile)) {
            tempFile.delete()
            throw FileIsNotAnImageException()
        }

        if (file.exists()) {
            file.delete()
        }
        tempFile.renameTo(file)

        return id
    }

    private fun getFileName(id: String) = "$id.png"
    private fun getTempFileName(fileName: String) = "$fileName.${random.nextLong()}.uploading"


    private fun writeToDisk(inputStream: InputStream, destination: File) {
        val out = FileOutputStream(destination)
        val buffer = ByteArray(1024)
        var sizeSoFar = 0L
        do {
            val bytesRead = inputStream.read(buffer)
            if (bytesRead > 0) {
                out.write(buffer, 0, bytesRead)
            }
            sizeSoFar += bytesRead
        } while (bytesRead > 0 && sizeSoFar < maxUploadBytes)

        inputStream.close()
        out.close()

        if (sizeSoFar >= maxUploadBytes) {
            destination.delete()
            throw FileExceedsMaxSizeException(sizeSoFar, maxUploadBytes)
        }
    }

    private fun isImage(file: File): Boolean = try {
        ImageIO.read(file)
        true
    } catch (ex: Exception) {
        false
    }


    override fun getImageAsResponse(id: String, headers: HttpHeaders): ResponseEntity<InputStreamResource> {
        val fileName = getFileName(id)
        val file = File(uploadsFolderPath, fileName)

        return if (file.exists()) {
            headers.contentLength = file.length()

            ResponseEntity.ok(InputStreamResource(FileInputStream(file)))
        } else {
            ResponseEntity.notFound().build()
        }
    }

}