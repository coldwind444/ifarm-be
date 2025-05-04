package com.dadn.be.ifarm.dto.response

data class WateringStatisticResponse(
    var duration : String? = null,
    var waterConsumption : Double? = null,
    var waterTimes : Int? = null
)
