package org.ttn.ecommerce.services.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    @Autowired
    UserRepository userRepository;

    public  String uploadImage(String email, MultipartFile multipartFile) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Path uploadPath = Paths.get("/home/kamlesh/Pictures/ecommerce_image");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve( userEntity.getId()+"."+multipartFile.getContentType());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file " , ioe);
        }
        return "Image Uploaded Successfully";
    }
}
