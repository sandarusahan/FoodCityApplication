package com.foodcityapp.esa.foodcityapplication;

/**
 * Created by sanda on 25/10/2017.
 */
     public class Product {
        private String prodId;
        private String prodName;
        private String prodPrice;
        private String prodImg;
        private String prodCat;
        private String prodDesc;
        private String prodQty;

        public Product() {
        }

        public Product(String prodId, String prodName, String prodPrice, String prodImg, String prodCat, String prodDesc, String prodQty) {

            this.prodId = prodId;
            this.prodName = prodName;
            this.prodPrice = prodPrice;
            this.prodImg = prodImg;
            this.prodCat = prodCat;
            this.prodDesc = prodDesc;
            this.prodQty = prodQty;
        }

        public String getProdName() { return prodName; }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getProdPrice() {
            return prodPrice;
        }

        public void setProdPrice(String prodPrice) {
            this.prodPrice = prodPrice;
        }

        public String getProdImg() {
            return prodImg;
        }

        public void setProdImg(String prodImg) {
            this.prodImg = prodImg;
        }

        public String getProdCat() {
            return prodCat;
        }

        public void setProdCat(String prodCat) {
            this.prodCat = prodCat;
        }

        public String getProdDesc() {
            return prodDesc;
        }

        public void setProdDesc(String prodDesc) {
            this.prodDesc = prodDesc;
        }

        public String getProdQty() {
            return prodQty;
        }

        public void setProdQty(String prodQty) {
            this.prodQty = prodQty;
        }
    }

