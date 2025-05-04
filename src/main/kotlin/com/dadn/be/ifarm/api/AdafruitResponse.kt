package com.dadn.be.ifarm.api

import com.dadn.be.ifarm.entity.HumidityData
import com.dadn.be.ifarm.entity.TemperatureData
import com.dadn.be.ifarm.entity.WaterData
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class AdafruitResponse(
    var id : String,
    var value : String,
    var feed_id : Long,
    var feed_key : String,
    var created_at : String,
    var created_epoch : Long,
    var expiration: String
){
    fun zulu2GMT7(input : String) : String {
        var zuluTime = ZonedDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
        val gmt7plus = zuluTime.plusHours(7)

        return gmt7plus.format(DateTimeFormatter.ISO_DATE_TIME).toString()
    }

    fun toHumidity() : HumidityData{
        val vnTime = zulu2GMT7(created_at)

        val date : String = vnTime.substring(0, 10)
        val time = vnTime.substring(11, 19)

        return HumidityData(
            id = id,
            value = value,
            date = date,
            time = time
        )
    }

    fun toTemperature() : TemperatureData{
        val vnTime = zulu2GMT7(created_at)

        val date : String = vnTime.substring(0, 10)
        val time = vnTime.substring(11, 19)

        return TemperatureData(
            id = id,
            value = value,
            date = date,
            time = time
        )
    }

    fun toWater() : WaterData{
        val vnTime = zulu2GMT7(created_at)

        val date : String = vnTime.substring(0, 10)
        val time = vnTime.substring(11, 19)

        return WaterData(
            id = id,
            value = value,
            date = date,
            time = time
        )
    }
}


