package com.example.loginfirebase

class Category {
    var CategoryId: String? = null
    var CategoryName: String? = null
    var CategoryImage: String? = null
    constructor() {}
    constructor(CategoryId: String?, CategoryName: String?, CategoryImage: String?) {
        this.CategoryId = CategoryId
        this.CategoryName = CategoryName
        this.CategoryImage = CategoryImage
    }
}