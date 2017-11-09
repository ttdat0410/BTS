package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class CardviewObject {
    private String nhietdo, doam, mucnuoc, baochay, khoi,cua,tenTram;
    Integer id;

    public CardviewObject() {
    }

    public CardviewObject(Integer id , String tenTram, String _nhietdo, String _doam, String _mucnuoc,
                          String _baochay, String _khoi, String _cua ){
        this.tenTram = tenTram;
        this.id = id;
        this.nhietdo = _nhietdo;
        this.doam = _doam;
        this.mucnuoc = _mucnuoc;
        this.baochay = _baochay;
        this.khoi = _khoi;
        this.cua = _cua;
    }
    public void setTenTram(String tenTram) {
        this.tenTram = tenTram;
    }

    public void setNhietdo(String nhietdo) {
        this.nhietdo = nhietdo;
    }
    public void setDoam(String doam) {
        this.doam = doam;
    }
    public void setMucnuoc(String mucnuoc) {
        this.mucnuoc = mucnuoc;
    }

    public void setBaochay(String baochay) {
        this.baochay = baochay;
    }

    public void setKhoi(String khoi) {
        this.khoi = khoi;
    }

    public void setCua(String cua) {
        this.cua = cua;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNhietdo(){
        return nhietdo;
    }
    public String getDoam(){
        return doam;
    }
    public String getMucnuoc(){
        return mucnuoc;
    }
    public String getBaochay(){
        return baochay;
    }
    public String getKhoi(){
        return khoi;
    }
    public String getCua(){
        return cua;
    }
    public Integer getId(){
        return id;
    }
    public String getTenTram() {
        return tenTram;
    }
}
