package com.example.notiup

class DropdownList {
    private var word: String? = null
    private var imageRes: Int = 0

    fun getWord(): String? {
        return word
    }

    fun setWord(word: String) {
        this.word = word
    }

    fun getImageRes(): Int {
        return imageRes
    }

    fun setImageRes(imageRes: Int) {
        this.imageRes = imageRes
    }
}
