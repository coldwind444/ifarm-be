package com.dadn.be.ifarm.entity

data class WaterData(
    var id : String? = null,
    var value : String? = null,
    var date : String? = null,
    var time : String? = null,
    var feedKey : String = "v3"
)
