package org.ttn.ecommerce.controller.category;

@Entity
public class CategoryMetadataFieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "categoryMetaDataId")
    private CategoryMetaData categoryMetaData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryMetaData getCategoryMetaData() {
        return categoryMetaData;
    }

    public void setCategoryMetaData(CategoryMetaData categoryMetaData) {
        this.categoryMetaData = categoryMetaData;
    }
}