package com.example.demo.ml.dto;

public class DataQualityReport {
    private long totalRecords;
    private long validRecords;
    private int missingValuesFound;
    private int duplicatesFound;
    private int outliersDetected;
    private double dataQualityScore;

    public DataQualityReport() {}

    public DataQualityReport(long totalRecords, long validRecords, int missingValuesFound, int duplicatesFound, int outliersDetected, double dataQualityScore) {
        this.totalRecords = totalRecords;
        this.validRecords = validRecords;
        this.missingValuesFound = missingValuesFound;
        this.duplicatesFound = duplicatesFound;
        this.outliersDetected = outliersDetected;
        this.dataQualityScore = dataQualityScore;
    }

    public long getTotalRecords() { return totalRecords; }
    public long getValidRecords() { return validRecords; }
    public int getMissingValuesFound() { return missingValuesFound; }
    public int getDuplicatesFound() { return duplicatesFound; }
    public int getOutliersDetected() { return outliersDetected; }
    public double getDataQualityScore() { return dataQualityScore; }

    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }
    public void setValidRecords(long validRecords) { this.validRecords = validRecords; }
    public void setMissingValuesFound(int missingValuesFound) { this.missingValuesFound = missingValuesFound; }
    public void setDuplicatesFound(int duplicatesFound) { this.duplicatesFound = duplicatesFound; }
    public void setOutliersDetected(int outliersDetected) { this.outliersDetected = outliersDetected; }
    public void setDataQualityScore(double dataQualityScore) { this.dataQualityScore = dataQualityScore; }
}
