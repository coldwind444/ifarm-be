package com.dadn.be.ifarm.service

import com.dadn.be.ifarm.dto.response.HumidityResponse
import com.dadn.be.ifarm.entity.HumidityData
import com.dadn.be.ifarm.mapper.HumidityMapper
import com.dadn.be.ifarm.repository.HumidityRepository
import com.dadn.be.ifarm.utils.Utils
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class HumidityService(
    private val humidityRepository : HumidityRepository,
    private val utils : Utils
){
    private final val mapper = HumidityMapper()

    fun getCurrentHumidity() : HumidityResponse? {
        val data : HumidityData? =  humidityRepository.fetchLast()
        if(data != null){
            val value = data.value?.toDouble() ?: -1.0
            return mapper.toHumidityResponse(data, utils.getHumidDescription(value))
        }
        return HumidityResponse()
    }

    fun getAllData() : List<HumidityResponse>{
        val dataList : List<HumidityData> = humidityRepository.fetchAll()
        return dataList.map { mapper.toHumidityResponse(it, utils.getHumidDescription(it.value?.toDouble() ?: -1.0)) }
    }

    fun getFilterData(start : String, end : String) : List<HumidityResponse>{
        val dataList : List<HumidityData> = humidityRepository.fetchFilter(start,end)
        return dataList.map { mapper.toHumidityResponse(it, utils.getHumidDescription(it.value?.toDouble() ?: -1.0)) }
    }

    fun getMostRecent() : String? {
        return humidityRepository.fetchMostRecent()
    }
}