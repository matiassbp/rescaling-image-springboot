package com.images.rescaling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.swagger.annotations.Api;
import net.coobird.thumbnailator.Thumbnails;

@RestController
@Api(value = "Image Resizer API", tags = "Image Resizer")
@CrossOrigin
public class ImageController {

    @PostMapping("/resize")
    public ResponseEntity<byte[]> resizeImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "width", defaultValue = "1920") int width,
            @RequestParam(value = "height", defaultValue = "1080") int height
    ) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] resizedImageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "resized_image.jpg");

            return new ResponseEntity<>(resizedImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/resize-no-aspect-ratio")
    public ResponseEntity<byte[]> resizeImageWithoutAspectRatio(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "width", defaultValue = "1920") int width,
            @RequestParam(value = "height", defaultValue = "1080") int height
    ) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .keepAspectRatio(false)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] resizedImageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "resized_image_without_aspect_ratio.jpg");

            return new ResponseEntity<>(resizedImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}