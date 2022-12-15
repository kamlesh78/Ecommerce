package org.ttn.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.dto.responseDto.ProductVariationResponseDto;
import org.ttn.ecommerce.entity.product.Product;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.services.ProductService;
import org.ttn.ecommerce.services.ProductVariationService;
import org.ttn.ecommerce.services.SellerService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    ProductService productService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductVariationService productVariationService;

    @Autowired
    ProductRepository productRepository;


    /**
     * @Probem : Add Product
     * @Constraint : Product Name Should Be Unique WithRespect TO {Category,Brand,Seller}
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("add/product/{categoryId}")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @PathVariable("categoryId") Long categoryId, Authentication authentication) {
        log.info("{Add Category   Fields}");
        String email = authentication.getName();
        return productService.addProduct(product, categoryId, email);

    }


    /**
     * @Problem : Add Product Variation
     * @Constraint : {Quantity,Price} should be Greater then 0
     * Each metadata field value sent should be from within the possible field values defined for that field in the category
     */

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("add/product-variation")
    public ResponseEntity<?> createProductVariation(@RequestBody ProductVariationDto productVariationDto,Authentication authentication) {

        return productVariationService.createProductVariation(productVariationDto);
    }


    /**
     * @Problem : View A Product By Its <<ID>>
     * @Constraints : Log IN Used Should be Owner Of Product
     * @Output : Product details along with selected category details
     */

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("view/product/{id}")
    public ResponseEntity<?> viewProductById(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        String email = authentication.getName();
        return productService.viewProductById(id, email);
    }


    /**
     * @Probelem : View Product Variation By Its <<ID>>
     * @OutPut : Product Variation with Parent Product Details
     */

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("view/product-variation/{variationId}")
    public ProductVariationResponseDto viewProductVariation(@PathVariable("variationId") Long productVariationId, Authentication authentication) {
        String email = authentication.getName();
        return productVariationService.getProductVariation(productVariationId, email);
    }


    /**
     * @Problem : View All Products Created By Seller
     * @Output : All non-deleted product with Category details
     */
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("view/all-products")
    public List<ProductResponseDto> viewAllProductsOfSeller(Authentication authentication) {

        String email = authentication.getName();
        return productVariationService.viewAllProductsOfSeller(email);

    }


    /**
     * @Problem : View All Product Variations For A Product
     * @Outptut : Product Variations OF The Product
     */
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("seller/view/product-variation/{productId}")
    public ResponseEntity<?> viewProductVariationOfProduct(@PathVariable("productId") Long productId) {

        return productVariationService.viewProductVariationByProduct(productId);
    }


    /**
     * @Problem : Delete Product
     * @Output : User Should Be Owner Of The Product
     */
    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("delete/product/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        return productService.deleteProduct(email, id);

    }

    /**
     * @Problem :  (1) Update Product
     * (2) Check If Updatable Product Name IS UNIQUE
     * WITH Respect TO  {BRAND , CATEGORY, SELLER} Combination
     * @Output :  Update Product Or Return Error If Any .
     */

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/update/product/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @RequestBody Product product, Authentication authentication) {

        String email = authentication.getName();
        return productService.updateProduct(email, productId, product);
    }

    /**
     * @Consumer  : <<Customer>>
     * @Problem   : Product Should be Valid And Non Deleted
     * @Output    : List of all products, along with each product's category details,
     * *       all variations primary images
     */

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("customer/view/product/{id}")
    public ResponseEntity<?> viewCustomerProduct(@PathVariable Long id) {

        return productService.customerViewProduct(id);
    }


    /**
     * @Consumer : <<Customer>>
     * @Problem  : Category Should be Valid And Leaf Node
     * @Output   : List of all products, along with each product's category details,
     *           all variations primary images
     */

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("customer/product/{categoryId}")
    public List<ProductResponseDto> viewCustomerAllProducts(@PathVariable("categoryId") Long categoryId) {
        return productService.retrieveProducts(categoryId);
    }


    /**      Similar Products    */


    /**
     * @Consumer : <<Admin>>
     * @Problem : View A Product By ITs Supplied <<ID>>
     * @Output : Product details along with product's selected category details,
     * all variations primary images
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/view/product/{id}")
    public ResponseEntity<?> viewProductByIdForAdmin(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        String email = authentication.getName();
        return productService.adminViewProductById(id, email);
    }


    /**
     * @Consumer : <<Admin>>
     * @Problem : View All Product
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/view/all-product")
    public ResponseEntity<?> adminViewAllProducts() throws Exception {

        return productService.adminViewAllProducts();
    }


    /**
     * @Consumer : <<Admin>>
     * @Problem : Activate Product By ITs  <<ID>>
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/activate/product/{productId}")
    public String activateProduct(@PathVariable("productId") long productId) {

        return productService.activateProduct(productId);
    }

    /**
     * @Consumer : <<Admin>>
     * @Problem : DeActivate Product By ITs  <<ID>>
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/deactivate/product/{productId}")
    public String deactivateProduct(@PathVariable("productId") Long id) {

        return productService.deactivateProduct(id);
    }


}
