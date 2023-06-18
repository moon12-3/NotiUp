package com.example.notiup.viewModel

data class TodoModel(
    var isCheck : Boolean,
    var name : String
) {
    constructor() : this(false, "")
}