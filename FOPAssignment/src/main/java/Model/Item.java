/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
public class Item {
    public int item_Code;
    public String item;
    public String unit;
    public String item_Group;
    public String item_Category;

    //Default Constructor
    public Item(){}
    
    //Parameterized Constructor
    public Item(int item_Code, String item, String unit, String item_Group, String item_Category) {
        this.item_Code = item_Code;
        this.item = item;
        this.unit = unit;
        this.item_Group = item_Group;
        this.item_Category = item_Category;
    }

    //Getter and Setter Method
    public int getItem_Code() {
        return item_Code;
    }

    public void setItem_Code(int item_Code) {
        this.item_Code = item_Code;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItem_Group() {
        return item_Group;
    }

    public void setItem_Group(String item_Group) {
        this.item_Group = item_Group;
    }

    public String getItem_Category() {
        return item_Category;
    }

    public void setItem_Category(String item_Category) {
        this.item_Category = item_Category;
    }
    
}
