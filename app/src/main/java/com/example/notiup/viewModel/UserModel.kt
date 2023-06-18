package com.example.notiup.viewModel

data class UserModel(
    var name: String,
    var achieve_cnt: Int
) {
    constructor() : this("", 0)
}