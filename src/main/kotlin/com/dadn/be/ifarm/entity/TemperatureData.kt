package com.dadn.be.ifarm.entity

data class TemperatureData(
    var id : String? = null,
    var value : String? = null,
    var date : String? = null,
    var time : String? = null,
    var feedKey : String = "v1"
)
