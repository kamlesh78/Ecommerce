package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.dto.product.responseDto.ProductResponseDto;
import org.ttn.ecommerce.dto.product.responseDto.ProductVariationResponseDto;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.entities.product.ProductVariation;
import org.ttn.ecommerce.services.product.ProductService;
import org.ttn.ecommerce.services.SellerDaoService;
import org.ttn.ecommerce.services.product.ProductVariationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    ProductService productService;

    @Autowired
    SellerDaoService sellerDaoService;

    @Autowired
    ProductVariationService productVariationService;


    /**         Add Product      */
    @PostMapping("add/product/{categoryId}")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @PathVariable("categoryId") Long categoryId, HttpServletRequest request){

        String email = sellerDaoService.emailFromToken(request);
        return  productService.addProduct(product,categoryId,email);

    }


    /**     View Product By Product Id      */
    @GetMapping("view/product/{id}")
    public Product viewProductById(@PathVariable("id") Long id,HttpServletRequest httpServletRequest) throws Exception{
        String email = sellerDaoService.emailFromToken(httpServletRequest);
        return productService.viewProductById(id,email);
    }

    /**            Add Product Variation       */
    @PostMapping("add/product-variation")
    public ResponseEntity<?> createProductVariation(@RequestBody ProductVariationDto productVariationDto){

       return productVariationService.createProductVariation(productVariationDto);
    }


    /**         View Product Variation By Id        */
    @GetMapping("view/product-variation/{variationId}")
    public ProductVariationResponseDto viewProductVariation(@PathVariable("variationId") Long productVariationId, HttpServletRequest httpServletRequest){
        String email = sellerDaoService.emailFromToken(httpServletRequest);
       // return new ResponseEntity<>(email,HttpStatus.OK);
              return productVariationService.getProductVariation(productVariationId,email);
    }

    /**         View All Products    By Seller   */

    @GetMapping("view/all-products")
    public List<ProductResponseDto> viewAllProduct(HttpServletRequest httpServletRequest){
        String email = sellerDaoService.emailFromToken(httpServletRequest);
        return productVariationService.viewAllProducts(email);

    }

    /**         Delete Product          */
    @DeleteMapping("delete/product/{id}")
    public String deleteProduct(@Param("id") Long id, HttpServletRequest httpServletRequest){
        String email = sellerDaoService.emailFromToken(httpServletRequest);
        return productService.deleteProduct(email,id);

    }

    /**                     Update Product                  */
    @PutMapping("/update/product/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @RequestBody Product product,HttpServletRequest httpServletRequest){
        String email = sellerDaoService.emailFromToken(httpServletRequest);

        return productService.updateProduct(email,productId,product);
    }


}
