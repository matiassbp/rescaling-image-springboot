package com.images.rescaling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

@RestController
@CrossOrigin
public class ImageController {

    @PostMapping("/ajustar-tamano-con-relacion-aspecto")
    @Operation(
        summary = "Redimensionar imagen respetando la relación de aspecto",
        description = "Redimensiona una imagen manteniendo su relación de aspecto y ajusta su calidad."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Imagen redimensionada exitosamente",
        content = @Content(mediaType = "image/jpeg")
    )
    public ResponseEntity<byte[]> redimensionarImagen(
            @RequestParam("file") @RequestBody(description = "Seleccione un archivo", required = true, content = @Content(schema = @Schema(type = "string", format = "binary"))) MultipartFile file,
            @RequestParam(value = "width", defaultValue = "1920") int width,
            @RequestParam(value = "height", defaultValue = "1080") int height,
            @RequestParam(value = "quality", defaultValue = "1.0") double quality
    ) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .outputQuality(quality)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] imagenRedimensionadaBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "imagen_redimensionada.jpg");

            return new ResponseEntity<>(imagenRedimensionadaBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/ajustar-tamano-sin-relacion-aspecto")
    @Operation(summary = "Redimensionar imagen ignorando la relación de aspecto", description = "Redimensiona una imagen sin mantener la relación de aspecto y ajusta su calidad.")
    @ApiResponse(responseCode = "200", description = "Imagen redimensionada exitosamente", content = @Content(mediaType = "image/jpeg"))
    public ResponseEntity<byte[]> redimensionarImagenSinRelacionAspecto(
            @RequestParam("file") @RequestBody(description = "Seleccione un archivo", required = true, content = @Content(schema = @Schema(type = "string", format = "binary"))) MultipartFile file,
            @RequestParam(value = "width", defaultValue = "1920") int width,
            @RequestParam(value = "height", defaultValue = "1080") int height,
            @RequestParam(value = "quality", defaultValue = "1.0") double quality) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .keepAspectRatio(false)
                    .outputQuality(quality) // Ajusta la calidad de la imagen
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] imagenRedimensionadaBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "imagen_redimensionada_sin_relacion_aspecto.jpg");

            return new ResponseEntity<>(imagenRedimensionadaBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/rotar")
    @Operation(summary = "Rotar la imagen", description = "Rota la imagen en el sentido de las agujas del reloj según el ángulo proporcionado.")
    @ApiResponse(responseCode = "200", description = "Imagen rotada exitosamente", content = @Content(mediaType = "image/jpeg"))
    public ResponseEntity<byte[]> rotarImagen(
            @RequestParam("file") @RequestBody(description = "Seleccione un archivo", required = true, content = @Content(schema = @Schema(type = "string", format = "binary"))) MultipartFile file,
            @RequestParam(value = "angle", defaultValue = "90") int angle) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .rotate(angle)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] rotatedImageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "imagen_rotada.jpg");

            return new ResponseEntity<>(rotatedImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mejorar-calidad")
    @Operation(
        summary = "Mejorar la calidad de la imagen",
        description = "Mejora la calidad de la imagen sin cambiar su tamaño ni relación de aspecto."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Imagen con calidad mejorada exitosamente",
        content = @Content(mediaType = "image/jpeg")
    )
    public ResponseEntity<byte[]> mejorarCalidadImagen(
            @RequestParam("file") @RequestBody(description = "Seleccione un archivo", required = true, content = @Content(schema = @Schema(type = "string", format = "binary"))) MultipartFile file,
            @RequestParam(value = "quality", defaultValue = "1.0") double quality) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .outputQuality(quality)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            byte[] improvedQualityImageBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", "imagen_calidad_mejorada.jpg");

            return new ResponseEntity<>(improvedQualityImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
