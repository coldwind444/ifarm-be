package com.dadn.be.ifarm.mapper

import com.dadn.be.ifarm.dto.response.TemperatureResponse
import com.dadn.be.ifarm.entity.TemperatureData

class TemperatureMapper {
    fun toTemperatureResponse(obj : TemperatureData, description : String) : TemperatureResponse{
        return TemperatureResponse(
            value = obj.value,
            date = obj.date,
            time = obj.time,
            description = description,
            id = obj.id
        )
    }
}