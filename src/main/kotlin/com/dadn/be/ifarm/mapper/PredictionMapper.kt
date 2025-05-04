package com.dadn.be.ifarm.mapper

import com.dadn.be.ifarm.dto.response.PredictionResponse
import com.dadn.be.ifarm.entity.PredictedData
import java.time.format.DateTimeFormatter

class PredictionMapper {
    fun toPredictionResponse(data : PredictedData, description: String) : PredictionResponse {
        return PredictionResponse(
            data = data.value,
            dateTime = data.dateTime.format(DateTimeFormatter.ISO_DATE_TIME),
            description = description
        )
    }
}