package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.images

import notFound
import ok
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

/**
 * Service that stores images (PNG, JPEG and GIF) of a specified max size in a specified folder
 */
@Service
class SimpleImagesService(
        @Value("\${uploads.maxSize}")
        private val maxUploadMB: Long,

        @Value("\${uploads.folder}")
        private val uploadsFolderPath: String
) : ImagesService {

    private val maxUploadBytes = megaBytesToBytes(maxUploadMB)

    /**
     * Random object used to generate names
     */
    private val random = Random()

    /**
     * Choose a unique name for the file that will contain the image and stores the image in it.
     * If the image is not a valid PNG, JPEG or GIF an Exception will be thrown
     */
    override fun storeImage(inputStream: InputStream): String {
        val id              = UUID.randomUUID().toString()
        val fileName        = getFileName(id)
        val tempFileName    = getTempFileName(fileName)

        /**
         * We store the image as a temporary file until we've verified that it's an actual image
         */

        val file        = File(uploadsFolderPath, fileName)
        val tempFile    = File(uploadsFolderPath, tempFileName)

        tempFile.parentFile.mkdir()
        writeToDiskIfDontExceedsMaxSize(inputStream, tempFile)

        // Check that it's an actual image
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

    /**
     * Read the content from an input stream and writes it to the specified file. If more bytes that the maximum are read
     * from the stream:
     * - The destination file will be closed and deleted
     * - The input stream will be closed
     * - An exception will be thrown
     */
    private fun writeToDiskIfDontExceedsMaxSize(inputStream: InputStream, destination: File) {
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

    /**
     * Tries to read the file as an image and return false if it fails
     */
    private fun isImage(file: File): Boolean = try {
        ImageIO.read(file)
        true
    } catch (ex: Exception) {
        false
    }

    /**
     * Read an image, given it's id, from the disk and return a input stream from which the image can be read.
     * Headers of the HTTP request are needed, to set the image size.
     */
    override fun getImageAsResponse(id: String, headers: HttpHeaders): ResponseEntity<InputStreamResource> {
        val fileName = getFileName(id)
        val file = File(uploadsFolderPath, fileName)

        return if (file.exists()) {
            headers.contentLength = file.length()

            ok(InputStreamResource(FileInputStream(file)))
        } else {
            notFound()
        }
    }

}

/**
 * Utility function to convert MB into Bytes
 */
private fun megaBytesToBytes(MB: Long) = MB * 1024 * 1024