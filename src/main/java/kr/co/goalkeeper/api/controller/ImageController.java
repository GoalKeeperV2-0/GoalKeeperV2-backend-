package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.service.port.CertificationGetService;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final CertificationGetService certificationGetService;

    public ImageController(CertificationGetService certificationGetService) {
        this.certificationGetService = certificationGetService;
    }
    @GetMapping(value = "/certification/{certificationId:[0-9]+}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCertificationImage(@PathVariable long certificationId) throws IOException {
        Certification certification = certificationGetService.getCertificationById(certificationId);
        String imagePath = certification.getPicture();
        InputStream imageInputStream = new FileInputStream(imagePath);
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        // Set headers
        imageInputStream.close();
        // Return response entity
        return new ResponseEntity<>(imageBytes, HttpStatus.OK);
    }
}
