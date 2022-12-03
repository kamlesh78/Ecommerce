package org.ttn.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.product.CategoryDto;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.entity.user.Seller;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.entity.category.Category;
import org.ttn.ecommerce.entity.product.Product;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.exception.ProductNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository.SellerRepository;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService implements org.ttn.ecommerce.services.ProductService {

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

    public static List<ProductResponseDto> staticList= new ArrayList<>();
    @Override
    public ResponseEntity<?> addProduct(Product product, Long categoryId, String email){

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


    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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


//    public List<ProductResponseDTO> adminViewAllProducts(){
//
//        List<Product> products = productRepository.findAll();
//         if(products.isEmpty()){
//            throw new BadRequestException(messageSource.getMessage("api.error.productNotFound",null,Locale.ENGLISH));
//        }
//
//         List<ProductResponseDTO> productResponseDTOList= new ArrayList<>();
//        for(Product product: products){
//
//            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
//
//            productResponseDTO.setId(product.getId());
//            productResponseDTO.setName(product.getName());
//            productResponseDTO.setBrand(product.getBrand());
//            productResponseDTO.setDescription(product.getDescription());
//            productResponseDTO.setIsActive(product.isActive());
//            productResponseDTO.setIsCancellable(product.isCancellable());
//            productResponseDTO.setIsReturnable(product.isReturnable());
//            productResponseDTO.setCategory(product.getCategory());
//            productResponseDTOList.add(productResponseDTO);
//        }
//        return productResponseDTOList;
//
//    }
    @Override
    public ResponseEntity<?> customerViewProduct(Long productId){
        //ProductResponseDTO
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        if (product.isDeleted() || !product.isActive()  ){
            return new ResponseEntity<>("Either Product is not active or Product is deleted",HttpStatus.BAD_REQUEST);

        }

        ProductResponseDto productResponse = new ProductResponseDto();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setBrand(product.getBrand());
        productResponse.setDescription(product.getDescription());
        productResponse.setActive(product.isActive());
        productResponse.setReturnable(product.isReturnable());
        productResponse.setCancellable(product.isCancellable());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        productResponse.setCategory(categoryDto);

        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<List<List<ProductResponseDto>>> viewAllProductOfProduct(Long categoryId){

        List<List<ProductResponseDto>> output= new ArrayList<>();
        Category category = categoryRepository.findById(categoryId)
                 .orElseThrow(()->new CategoryNotFoundException("Category Not Found"));


      //  if (category.getSubCategory()!=null){
            //throw new BadRequestException(messageSource.getMessage("api.error.notLeafNode",null,Locale.ENGLISH));
            List<Category> subCategories = category.getSubCategory();
            List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
            for(Category category1 : subCategories){
                for(ProductResponseDto productResponseDto : staticList){
                    productResponseDto.getName();
                }
            }


       // }



        return new ResponseEntity<>(output,HttpStatus.OK);
//        List<Product> products = productRepository.findByCategory(category);
//        // check if records are present
//        if(products.isEmpty()){
//            throw new BadRequestException(messageSource.getMessage("api.error.productNotFound",null,Locale.ENGLISH));
//        }
//
//        // convert to appropriate DTO
//        List<ProductResponseDTO> productResponseDTOList= new ArrayList<>();
//        for(Product product: products){
//
//            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
//
//            productResponseDTO.setId(product.getId());
//            productResponseDTO.setName(product.getName());
//            productResponseDTO.setBrand(product.getBrand());
//            productResponseDTO.setDescription(product.getDescription());
//            productResponseDTO.setIsActive(product.isActive());
//            productResponseDTO.setIsCancellable(product.isCancellable());
//            productResponseDTO.setIsReturnable(product.isReturnable());
//            productResponseDTO.setCategory(product.getCategory());
//            productResponseDTOList.add(productResponseDTO);
//        }
//        return productResponseDTOList;

    }

//    public static List<ProductResponseDto> getLeafCategory(Category category,List<ProductResponseDto> productResponseDtoList){
//        if(category == null){
//
//            return new ArrayList<ProductResponseDto>();
//        }
//        if(category.getSubCategory() == null) {
//            System.out.println("not null");
//
//            List<Product> productList = category.getProducts();
//            List<ProductResponseDto> list = new ArrayList<>();
//            for(Product product : productList){
//                ProductResponseDto productResponseDto = new ProductResponseDto();
//                productResponseDto.setId(product.getId());
//                productResponseDto.setName(product.getName());
//                productResponseDto.setBrand(product.getBrand());
//                productResponseDto.setDescription(product.getDescription());
//                productResponseDto.setActive(product.isActive());
//                productResponseDto.setCancellable(product.isCancellable());
//                productResponseDto.setReturnable(product.isReturnable());
//                CategoryDto categoryDto =new CategoryDto();
//                categoryDto.setId(product.getCategory().getId());
//                categoryDto.setName(product.getCategory().getName());
//                System.out.println(productResponseDto.getName());
//                productResponseDto.setCategory(categoryDto);
//                list.add(productResponseDto);
//            }
//            return list;
//        }
//
//        for(Category category1 : category.getSubCategory()){
//             getLeafCategory(category1,productResponseDtoList);
//        }
//
//        return productResponseDtoList;
//
//    }
//
////
//    public List<Product> viewSimilarProducts(Long productId){
//
//        // check if ID is valid
//        Optional<Product> product = productRepository.findById(productId);
//        if (product == null || product.isEmpty()) {
//            throw new BadRequestException(messageSource.getMessage("api.error.invalidProductId",null,Locale.ENGLISH));
//        }
//
//        // check product status
//        if (product.get().isDeleted() || product.get().isActive() == false ){
//            throw new BadRequestException(messageSource.getMessage("api.error.productInactiveDeleted",null,Locale.ENGLISH));
//        }
//
//        // find similar products
//        Category associatedCategory = product.get().getCategory();
//        List<Product> similarProducts = new ArrayList<>();
//
//        // add other products associated to its category to similar list
//        List<Product> siblingProducts = productRepository.findByCategory(associatedCategory);
//
//        for(Product individualProduct: siblingProducts){
//            similarProducts.add(individualProduct);
//        }
//
//        if(similarProducts.size()==1){
//            throw new BadRequestException(messageSource.getMessage("api.error.similarProducts",null,Locale.ENGLISH));
//        }
//
//        return similarProducts;
//
//    }



 @Override
 public List<ProductResponseDto> retrieveProducts(Long id){

        int noOfCategory = categoryRepository.findById(id).get().getSubCategory().size();
        Category category = categoryRepository.findById(id).get();
        if(noOfCategory==0){
            List<Product> list = category.getProducts();
            List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
            for(Product product: list){
                ProductResponseDto dto = new ProductResponseDto();
                dto.setId(product.getId());
                dto.setDescription(product.getDescription());
                dto.setBrand(product.getBrand());
                dto.setActive(product.isActive());
                dto.setName(product.getName());
                dto.setDeleted(product.isDeleted());

                productResponseDtoList.add(dto);
            }
            return productResponseDtoList;

        }
        else{

            List<ProductResponseDto> allProducts =new ArrayList<>();
            for(Category category1 : category.getSubCategory()){

                List<ProductResponseDto> currProducts = retrieveProducts(category1.getId());

                for(ProductResponseDto productResponseDto : currProducts){

                    allProducts.add(productResponseDto);

                }

            }
            return allProducts;

        }
 }


}
