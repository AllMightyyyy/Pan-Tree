package org.zakariafarih.recipemakerbackend.dto.edamam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeSearchResponse {

    private int count;
    private List<Hit> hits;
    private Links _links;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Hit {
    private Recipe recipe;
    private Links _links;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Links {
    private Link self;
    private Link next;

    public Link getSelf() {
        return self;
    }

    public void setSelf(Link self) {
        this.self = self;
    }

    public Link getNext() {
        return next;
    }

    public void setNext(Link next) {
        this.next = next;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Link {
    private String href;
    private String title;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Images {
    private ImageInfo THUMBNAIL;
    private ImageInfo SMALL;
    private ImageInfo REGULAR;
    private ImageInfo LARGE;

    public ImageInfo getTHUMBNAIL() {
        return THUMBNAIL;
    }

    public void setTHUMBNAIL(ImageInfo THUMBNAIL) {
        this.THUMBNAIL = THUMBNAIL;
    }

    public ImageInfo getSMALL() {
        return SMALL;
    }

    public void setSMALL(ImageInfo SMALL) {
        this.SMALL = SMALL;
    }

    public ImageInfo getREGULAR() {
        return REGULAR;
    }

    public void setREGULAR(ImageInfo REGULAR) {
        this.REGULAR = REGULAR;
    }

    public ImageInfo getLARGE() {
        return LARGE;
    }

    public void setLARGE(ImageInfo LARGE) {
        this.LARGE = LARGE;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ImageInfo {
    private String url;
    private int width;
    private int height;

    // Getters and Setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Ingredient {
    private String text;
    private double quantity;
    private String measure;
    private String food;
    private double weight;
    private String foodId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Nutrients {
    private Map<String, NutrientInfo> nutrients;

    public Map<String, NutrientInfo> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Map<String, NutrientInfo> nutrients) {
        this.nutrients = nutrients;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class NutrientInfo {
    private String label;
    private double quantity;
    private String unit;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Digest {
    private String label;
    private String tag;
    private String schemaOrgTag;
    private double total;
    private boolean hasRDI;
    private double daily;
    private String unit;
    private List<Digest> sub;

    public boolean isHasRDI() {
        return hasRDI;
    }

    public void setHasRDI(boolean hasRDI) {
        this.hasRDI = hasRDI;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSchemaOrgTag() {
        return schemaOrgTag;
    }

    public void setSchemaOrgTag(String schemaOrgTag) {
        this.schemaOrgTag = schemaOrgTag;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDaily() {
        return daily;
    }

    public void setDaily(double daily) {
        this.daily = daily;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Digest> getSub() {
        return sub;
    }

    public void setSub(List<Digest> sub) {
        this.sub = sub;
    }
}
