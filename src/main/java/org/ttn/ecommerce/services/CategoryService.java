package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.repository.CategoryMetaDataFieldRepository;
import org.ttn.ecommerce.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {


//    @Autowired
//    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    public String createMetaDataField(CategoryMetaDataField metaDataField) {
        String name = metaDataField.getName();
        Optional<CategoryMetaDataField> categoryMetaDataField = categoryMetaDataFieldRepository.findByName(name);
        if(categoryMetaDataField.isPresent()){
            return "MetaData Field Already Exists! MetaData Filed Should Be Unique.";
        }else{

            categoryMetaDataFieldRepository.save(metaDataField);
            return "New MetaData Field Created.";
        }
    }
}
