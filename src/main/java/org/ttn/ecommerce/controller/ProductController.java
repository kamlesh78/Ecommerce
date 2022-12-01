package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.entities.product.ProductVariation;
import org.ttn.ecommerce.services.product.ProductService;
import org.ttn.ecommerce.services.SellerDaoService;
import org.ttn.ecommerce.services.product.ProductVariationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SellerDaoService sellerDaoService;

    @Autowired
    ProductVariationService productVariationService;

    @PostMapping("add/product/{categoryId}")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @PathVariable("categoryId") Long categoryId, HttpServletRequest request){

        String email = sellerDaoService.emailFromToken(request);
        return  productService.addProduct(product,categoryId,email);

    }

    @PostMapping("add/product-variation")
    public ResponseEntity<?> createProductVariation(@RequestBody ProductVariationDto productVariationDto){
        System.out.println(productVariationDto.getMetaData());
        return new ResponseEntity<>("a", HttpStatus.CREATED);
      //  return productVariationService.createProductVariation(productVariationDto);
    }


}
