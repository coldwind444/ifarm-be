package com.dadn.be.ifarm.service

import com.dadn.be.ifarm.dto.response.TemperatureResponse
import com.dadn.be.ifarm.entity.TemperatureData
import com.dadn.be.ifarm.mapper.TemperatureMapper
import com.dadn.be.ifarm.repository.TemperatureRepository
import com.dadn.be.ifarm.utils.Utils
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class TemperatureService(
    private val temperatureRepository: TemperatureRepository,
    private val utils: Utils,
) {
    private val mapper = TemperatureMapper()

    fun getCurrentTemperature() : TemperatureResponse? {
        val data : TemperatureData? =  temperatureRepository.fetchLast()

        if(data != null){
            val value : Double = data.value?.toDouble() ?: -1.0
            return mapper.toTemperatureResponse(data, utils.getTempDescription(value))
        }
        return TemperatureResponse()
    }

    fun getRecentTemperature() : String? {
        return temperatureRepository.fetchMostRecent()
    }

    fun getAllData() : List<TemperatureResponse>{
        val dataList : List<TemperatureData> = temperatureRepository.fetchAll()
        return dataList.map { mapper.toTemperatureResponse(it, utils.getTempDescription(it.value?.toDouble() ?: -1.0)) }
    }

    fun getFilterData(start : String, end : String) : List<TemperatureResponse>{
        val dataList : List<TemperatureData> = temperatureRepository.fetchFilter(start,end)
        return dataList.map { mapper.toTemperatureResponse(it, utils.getTempDescription(it.value?.toDouble() ?: -1.0)) }
    }
}