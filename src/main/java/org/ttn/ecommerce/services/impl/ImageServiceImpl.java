package org.ttn.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements org.ttn.ecommerce.services.ImageService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ImageResponse uploadImage(String email, MultipartFile multipartFile) throws IOException {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<String> validExtension = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png", "bmp"));

        String[] arr = multipartFile.getContentType().split("/");
        String fileType = arr[1];
        if (!validExtension.contains(fileType)) {
            // return "Invalid FileType -> Only [jpeg, jpg, bmp, png] FileTypes allowed";
            throw new FileNotFoundException("Invalid FileType -> Only [jpeg, jpg, bmp, png] FileTypes allowed");
        }

        Path uploadPath = Paths.get("/home/kamlesh/Pictures/ecommerce_image");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(userEntity.getId() + "." + arr[1]);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file ", ioe);
        }

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setFileName(userEntity.getId() + "." + arr[1]);
        imageResponse.setUrl(SecurityConstants.FILE_UPLOAD_URL);
        imageResponse.setMessage("Image Uploaded Successfully");
        return imageResponse;
        //return "Image Uploaded Successfully";
    }


    @Override
    public ResponseEntity<?> getImage(String email) {

        String dir = "/home/kamlesh/Pictures/ecommerce_image";

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found."));
        File file = new File(dir);
        for (File files : file.listFiles()) {


            String filesName = files.getName().split("\\.")[0];
            String fileType = "image/" + files.getName().split("\\.")[1];
            if (userEntity.getId() == Long.parseLong(filesName)) {
                byte[] bytes = new byte[(int) files.length()];

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(files);
                    fis.read(bytes);
                    System.out.println(bytes);
                } catch (Exception e) {
                    System.out.println(e.fillInStackTrace());
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.valueOf(fileType))
                        .body(bytes);

            }

        }

        return new ResponseEntity<>("cant display image", HttpStatus.BAD_REQUEST);

    }

    @Override
    public String getImagePath(UserEntity userEntity) {

        String fileName = "";
        try {
            Set<String> fileList = listFilesUsingJavaIO(SecurityConstants.FILE_UPLOAD_URL);
            for (String file : fileList) {
                if (file.startsWith(userEntity.getId().toString())) {
                    fileName = file;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return SecurityConstants.FILE_UPLOAD_URL + "/" + fileName;
    }


    @Override
    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}

