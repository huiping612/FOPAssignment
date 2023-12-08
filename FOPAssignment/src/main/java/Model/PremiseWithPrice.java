/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.time.LocalDate;

public class PremiseWithPrice {

    public int premise_Code;
    public String premise;
    public String address;
    public String premise_type;
    public String state;
    public String district;
    public double price;
    public LocalDate date;
    
    //Default Constructor
    public PremiseWithPrice() {}

    //Parameterized Constructor
    public PremiseWithPrice(int premise_Code, String premise, String address, String premise_type, String state, String district, double price, LocalDate date) {
        this.premise_Code = premise_Code;
        this.premise = premise;
        this.address = address;
        this.premise_type = premise_type;
        this.state = state;
        this.district = district;
        this.price = price;
        this.date = date;
    }

    public PremiseWithPrice(int premise_Code, String premise, String address, String premise_type, String state, String district, Double value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //Getter and Setter Method
    public int getPremise_Code() {
        return premise_Code;
    }

    public void setPremise_Code(int premise_Code) {
        this.premise_Code = premise_Code;
    }

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPremise_type() {
        return premise_type;
    }

    public void setPremise_type(String premise_type) {
        this.premise_type = premise_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}

