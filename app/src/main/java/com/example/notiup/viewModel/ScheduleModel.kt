package com.example.notiup.viewModel

data class ScheduleModel(
    var aName : String,
    var aMemo : String,
    var sDate : String,
    var sTime : String,
    val ord : String
) {
    constructor() : this("", "", "", "", "")
}