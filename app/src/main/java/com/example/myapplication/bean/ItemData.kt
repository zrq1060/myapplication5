package com.example.myapplication.bean

import java.io.Serializable

class ItemData : Serializable {
    val imageRes: Int
    val title: String?
    val description: String?

    constructor(imageRes: Int, title: String?, description: String?) {
        this.imageRes = imageRes
        this.title = title
        this.description = description
    }
}