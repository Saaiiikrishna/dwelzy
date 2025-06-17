package com.dwelzy.dwelzy.dto;

import java.math.BigDecimal;
import java.util.List;

public class ItemAnalysisResponse {
    
    private String itemType;
    private String itemCategory;
    private String description;
    private Double estimatedWeight;
    private Double estimatedLength;
    private Double estimatedWidth;
    private Double estimatedHeight;
    private Double estimatedVolume;
    private String fragility;
    private List<String> handlingInstructions;
    private BigDecimal estimatedPrice;
    private Double confidence;
    private String analysisId;
    
    // Constructors
    public ItemAnalysisResponse() {}
    
    // Getters and Setters
    public String getItemType() {
        return itemType;
    }
    
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getItemCategory() {
        return itemCategory;
    }
    
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public Double getEstimatedVolume() {
        return estimatedVolume;
    }
    
    public void setEstimatedVolume(Double estimatedVolume) {
        this.estimatedVolume = estimatedVolume;
    }
    
    public String getFragility() {
        return fragility;
    }
    
    public void setFragility(String fragility) {
        this.fragility = fragility;
    }
    
    public List<String> getHandlingInstructions() {
        return handlingInstructions;
    }
    
    public void setHandlingInstructions(List<String> handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }
    
    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }
    
    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public String getAnalysisId() {
        return analysisId;
    }
    
    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }
}
