/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.time.LocalDate;

public class PriceCatcher {

    public LocalDate date;
    public int premise_Code;
    public int item_Code;
    public double price;

    //Default Constructor
    public PriceCatcher(){}
    
    //Parameterized Constructor
    public PriceCatcher(LocalDate date, int premise_Code, int item_Code, double price) {
        this.date = date;
        this.premise_Code = premise_Code;
        this.item_Code = item_Code;
        this.price = price;
    }
    
    //Getter and Setter Method
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPremise_Code() {
        return premise_Code;
    }

    public void setPermise_Code(int premise_Code) {
        this.premise_Code = premise_Code;
    }

    public int getItem_Code() {
        return item_Code;
    }

    public void setItem_Code(int item_Code) {
        this.item_Code = item_Code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
