package com.example.notiup

data class ScheduleModel(
    var aName : String,
    var aMemo : String,
    var sDate : String
) {
    constructor() : this("", "", "")
}