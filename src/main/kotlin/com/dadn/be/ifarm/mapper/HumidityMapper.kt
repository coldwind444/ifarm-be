package com.dadn.be.ifarm.mapper

import com.dadn.be.ifarm.dto.response.HumidityResponse
import com.dadn.be.ifarm.entity.HumidityData
import org.springframework.context.annotation.Description

class HumidityMapper {
    fun toHumidityResponse(obj : HumidityData, description: String) : HumidityResponse {
        return HumidityResponse(
            value = obj.value,
            date = obj.date,
            time = obj.time,
            description = description,
            id = obj.id
        )
    }
}