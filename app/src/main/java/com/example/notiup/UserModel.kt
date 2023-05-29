package com.example.notiup

data class UserModel(
    var name: String,
    var achieve_cnt: Int
) {
    constructor() : this("", 0)
}