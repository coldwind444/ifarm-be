package com.dadn.be.ifarm.dto.response

data class TemperatureResponse(
    var value : String? = null,
    var id : String? = null,
    var description : String? = null,
    var time : String? = null,
    var date : String? = null,
)