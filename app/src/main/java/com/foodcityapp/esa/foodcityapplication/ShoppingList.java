package com.foodcityapp.esa.foodcityapplication;

/**
 * Created by sanda on 03/11/2017.
 */

public class ShoppingList {

    String listId;
    String listTitle;
    String listContent;

    public ShoppingList() {

    }

    public ShoppingList(String listId, String listTitle, String listContent) {
        this.listId = listId;
        this.listTitle = listTitle;
        this.listContent = listContent;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }
}
