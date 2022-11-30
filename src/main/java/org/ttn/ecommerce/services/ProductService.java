package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.repository.SellerRepository;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;

import java.util.List;

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
         *      Check If product is Unique WithRespect TO Category and Seller Or Not
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


}
