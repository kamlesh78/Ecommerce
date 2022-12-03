package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.entity.user.UserEntity;

import java.io.IOException;
import java.util.Set;

public interface ImageService {
    ImageResponse uploadImage(String email, MultipartFile multipartFile) throws IOException;

    ResponseEntity<?> getImage(String email);

    String getImagePath(UserEntity userEntity);

    Set<String> listFilesUsingJavaIO(String dir);
}
