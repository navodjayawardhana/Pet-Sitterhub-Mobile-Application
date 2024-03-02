package com.example.myapplication;

public class DataClass {

    public DataClass(String dataTitle, String dataDesc, String dataLang, String dataAddress, String dataPname,
                     String dataPage, String dataStart, String dataEnd,String dataFees, String dataBreed, String dataSex, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataAddress = dataAddress;
        this.dataPname = dataPname;
        this.dataPage = dataPage;
        this.dataBreed = dataBreed;
        this.dataSex = dataSex;
        this.dataImage = dataImage;
        this.key = key;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.dataFees = dataFees;

    }

    private String dataTitle;
    private String dataDesc;
    private String dataLang;
    private String dataAddress;
    private String dataPname;
    private String dataPage;
    private String dataBreed;
    private String dataSex;
    private String dataImage;
    private String key;
    private String dataStart;
    private String dataEnd;
    private String dataFees;
    private String careName;




    public String getCareName() {
        return careName;
    }

    public DataClass(String careName) {
        this.careName = careName;
    }

    public String getDataStart() {
        return dataStart;
    }

    public String getDataEnd() {
        return dataEnd;
    }

    public String getDataFees() {
        return dataFees;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataAddress() {
        return dataAddress;
    }

    public String getDataPname() {
        return dataPname;
    }

    public String getDataPage() {
        return dataPage;
    }

    public String getDataBreed() {
        return dataBreed;
    }

    public String getDataSex() {
        return dataSex;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }

    public String getDataImage() {
        return dataImage;
    }





    public DataClass() {
    }
}
