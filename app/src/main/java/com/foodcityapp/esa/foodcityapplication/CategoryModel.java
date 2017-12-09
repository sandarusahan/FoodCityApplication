package com.foodcityapp.esa.foodcityapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanda on 04/11/2017.
 */

public class CategoryModel {

    private String headerTitle;
    private ArrayList<Product> allItemsInCategory;

    public CategoryModel() {
    }

    public CategoryModel(String headerTitle, ArrayList<Product> allItemsInCategory) {
        this.headerTitle = headerTitle;
        this.allItemsInCategory = allItemsInCategory;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Product> getAllItemsInCategory() {

        return allItemsInCategory;
    }

    public void setAllItemsInCategory(ArrayList<Product> allItemsInCategory) {
        this.allItemsInCategory = allItemsInCategory;
    }
}
