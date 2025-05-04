package com.dadn.be.ifarm.api

import com.dadn.be.ifarm.entity.PredictedData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ModelResponse(
    var ds : String,
    var yhat : Double = 0.0,
    var yhat_upper : Double = 0.0,
    var yhat_lower : Double = 0.0,
){
    fun toPredictedData() : PredictedData {
        return PredictedData(
            dateTime = LocalDateTime.parse(ds, DateTimeFormatter.ISO_DATE_TIME),
            value = yhat
        )
    }
}
