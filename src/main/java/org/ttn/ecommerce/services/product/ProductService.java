package org.ttn.ecommerce.services.product;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.dto.product.CategoryDto;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.exception.ProductNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;
import org.ttn.ecommerce.services.EmailServicetry;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EmailServicetry emailServicetry;

    public ResponseEntity<?> addProduct(Product product,Long categoryId,String email){

        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Seller Not Found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException("Category Not Found"));

        /**
                Check if provided Category is leaf node or not
         */
        List<Category> subCategoryList = category.getSubCategory();
        if(subCategoryList.size()!=0){
            return new ResponseEntity<>("Only leaf Node Can Contain Product ",HttpStatus.BAD_REQUEST);
        }


        /**
         *      Check If product is Unique WithRespect TO Category , Seller And Brand Or Not
         */

        List<Product> productList = category.getProducts();
        if(productList.size()!=0){
            for(Product productCheck : productList){
                    if(productCheck.getSeller().getId()==seller.getId() && product.getName().equals(productCheck.getName()) && productCheck.getBrand().equals(product.getBrand())){

                        return new ResponseEntity<>("Product Name Can Not Be Added for Seller  Within same Category",HttpStatus.BAD_REQUEST);
                    }

            }
        }

        product.setSeller(seller);
        product.setCategory(category);
        product.setActive(false);

        productRepository.save(product);

        /**
         *      Email Admin To Activate The Product
         */

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("New Product Added");
            simpleMailMessage.setText(
            "Activate New Product "
            +"\n Product Details"
            +"\nProduct Id : " + product.getId()
            +"\nProduct Name : " + product.getName()
            +"\nSeller Name  : " + seller.getFirstName()
            +"\nCategory Name : + " +category.getName());
            simpleMailMessage.setTo("kamlesh.singh@tothenew.com");

            emailServicetry.sendEmail(simpleMailMessage);
        }catch (MailException ex){
            return new ResponseEntity<>("\"Cant Send Mail || Mailing server is down || Kindly wait\"",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("New Product Added Successfully", HttpStatus.OK);
    }


    public ResponseEntity<?> viewProductById(Long id, String email) throws Exception {

        UserEntity seller = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found For Given ProductID"));

        if(product.isDeleted()){

            return new ResponseEntity<>("Product Is Deleted",HttpStatus.BAD_REQUEST);
        }

        if(!(product.getSeller().getId() == seller.getId())){

            return new ResponseEntity<>("Only Owner Of Product Can View It",HttpStatus.BAD_REQUEST);
        }

        /* Set Category Of The Product */
        Category category = product.getCategory();
        CategoryDto categoryDto =new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setActive(product.isActive());
        productResponseDto.setBrand(product.getBrand());
        productResponseDto.setDeleted(product.isDeleted());
        productResponseDto.setCancellable(product.isCancellable());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setCategory(categoryDto);

        return new ResponseEntity<>(productResponseDto,HttpStatus.OK);
    }

    public String deleteProduct(String email, Long id) {

        UserEntity seller = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("No Product Found With Given ID"));

        /** Check If Current Seller Is Owner Of Product or Not */


        if(!(product.getSeller().getId()==seller.getId())){
            return "Only Owner Of Product Can Delete It Product";
        }
        productRepository.deleteById(id);
        return "Product Deleted";
    }

    public String updateProduct(String email, Long productId, Product productUpdate) {

     Product product = productRepository.findById(productId)
             .orElseThrow(()->new ProductNotFoundException("No Product Found For Given Id"));

        UserEntity seller = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(!(seller.getId()==product.getSeller().getId())){
            return "Only Owner Of Product IS Allow TO Update Product";
        }

        /**
         *      Check If product is Unique WithRespect TO Category and Seller Or Not
         */
        System.out.println(productUpdate.getBrand());
        if (productUpdate.getName() != null){
            List<Product> productList = product.getCategory().getProducts();
            if(productList.size()!=0){
                for(Product productCheck : productList){
                    if(productCheck.getSeller().getId()==seller.getId() && product.getName().equals(productCheck.getName()) && productCheck.getBrand().equals(product.getBrand())){

                        return "Product Name Can Not Be Added for Seller  Within same Category";
                    }

                }
            }
            product.setName(productUpdate.getName());
        }

        if (productUpdate.getDescription() != null)
            product.setDescription(productUpdate.getDescription());
        if (productUpdate.getBrand() !=  null)
            product.setBrand(productUpdate.getBrand());
        if(productUpdate.getName()!=null)
            product.setName(productUpdate.getName());

        if (productUpdate.isReturnable())
            product.setReturnable(productUpdate.isReturnable());
        if (product.isCancellable())
            product.setCancellable(productUpdate.isCancellable());
        productRepository.save(product);
        return "Product updated successfully";

    }

    public String activateProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("No Product Found For Given Id"));

        if(product.isActive()){
            return "Product is Already Activated";
        }

        product.setActive(true);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(product.getSeller().getEmail());
        mailMessage.setSubject("Product  Activated");
        mailMessage.setFrom("kamlesh.singh@tothenew.com");
        mailMessage.setText("Product Name :" + product.getName()
        + "\nStatus : Activated");
        emailServicetry.sendEmail(mailMessage);

        return "Product Activated";

    }

    public String deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("No Product Found For Given Id"));


         if (!product.isActive()) {
             return "Product is Already Deactivated";
         }

            product.setActive(false);
            productRepository.save(product);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(product.getSeller().getEmail());
            mailMessage.setSubject("Product Successfully Deactivated");
            mailMessage.setFrom("kamlesh.singh@tothenew.com");
            mailMessage.setText("Product has been deactivated " + product.getName());
            emailServicetry.sendEmail(mailMessage);

            return "Product deactivated successfully";

    }
}
