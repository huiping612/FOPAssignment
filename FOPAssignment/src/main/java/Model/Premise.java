/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
public class Premise {
    public int premise_Code;
    public String premise;
    public String address;
    public String premise_type;
    public String state;
    public String district;

    //Default Constructor
    public Premise(){}
    
    //Parameterized Constructor
    public Premise(int premise_Code, String premise, String address, String premise_type, String state, String district) {
        this.premise_Code = premise_Code;
        this.premise = premise;
        this.address = address;
        this.premise_type = premise_type;
        this.state = state;
        this.district = district;
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
    
}