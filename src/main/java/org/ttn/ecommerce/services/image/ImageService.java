package org.ttn.ecommerce.services.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ttn.ecommerce.dto.image.ImageResponse;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.security.SecurityConstants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    UserRepository userRepository;

    public ImageResponse uploadImage(String email, MultipartFile multipartFile) throws IOException {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<String> validExtension = new ArrayList<>(Arrays.asList("jpg","jpeg","png","bmp"));

        String[] arr = multipartFile.getContentType().split("/");
        String fileType = arr[1];
        if(!validExtension.contains(fileType)){
           // return "Invalid FileType -> Only [jpeg, jpg, bmp, png] FileTypes allowed";
            throw new FileNotFoundException("Invalid FileType -> Only [jpeg, jpg, bmp, png] FileTypes allowed");
        }

        Path uploadPath = Paths.get("/home/kamlesh/Pictures/ecommerce_image");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(userEntity.getId() + "."+arr[1]);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file " , ioe);
        }

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setFileName(userEntity.getId()+"."+arr[1]);
        imageResponse.setUrl(SecurityConstants.FILE_UPLOAD_URL);
        imageResponse.setMessage("Image Uploaded Successfully");
        return imageResponse;
        //return "Image Uploaded Successfully";
    }


    public ResponseEntity<?> getImage(String email){

        String dir="/home/kamlesh/Pictures/ecommerce_image";

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found."));
        File file = new File(dir);
        for(File files : file.listFiles()){


            String filesName = files.getName().split("\\.")[0];
            String fileType  = "image/" +files.getName().split("\\.") [1];
            if(userEntity.getId()==Long.parseLong(filesName)){
                byte[] bytes = new byte[(int) files.length()];

                FileInputStream fis = null;
                try {

                    fis = new FileInputStream(files);


                    fis.read(bytes);
                    System.out.println(bytes);
                }
                catch (Exception e){
                    System.out.println(e.fillInStackTrace());
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.valueOf(fileType))
                        .body(bytes);

            }

        }

        return new ResponseEntity<>("cant display image",HttpStatus.BAD_REQUEST);

    }
}

