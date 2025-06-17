package com.dwelzy.dwelzy.dto;

import jakarta.validation.constraints.NotNull;

public class ItemAnalysisRequest {
    
    @NotNull
    private String imageBase64;
    
    private String itemDescription;
    private Double estimatedWeight;
    private Double estimatedLength;
    private Double estimatedWidth;
    private Double estimatedHeight;
    
    // Constructors
    public ItemAnalysisRequest() {}
    
    // Getters and Setters
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public String getItemDescription() {
        return itemDescription;
    }
    
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    
    public Double getEstimatedWeight() {
        return estimatedWeight;
    }
    
    public void setEstimatedWeight(Double estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }
    
    public Double getEstimatedLength() {
        return estimatedLength;
    }
    
    public void setEstimatedLength(Double estimatedLength) {
        this.estimatedLength = estimatedLength;
    }
    
    public Double getEstimatedWidth() {
        return estimatedWidth;
    }
    
    public void setEstimatedWidth(Double estimatedWidth) {
        this.estimatedWidth = estimatedWidth;
    }
    
    public Double getEstimatedHeight() {
        return estimatedHeight;
    }
    
    public void setEstimatedHeight(Double estimatedHeight) {
        this.estimatedHeight = estimatedHeight;
    }
}
