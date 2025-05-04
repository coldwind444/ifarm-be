package com.dadn.be.ifarm.entity

data class HumidityData (
    var id : String? = null,
    var value : String? = null,
    var date : String? = null,
    var time : String? = null,
    var feedKey : String = "v2"
)
