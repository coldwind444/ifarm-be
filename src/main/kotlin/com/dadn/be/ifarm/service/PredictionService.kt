package com.dadn.be.ifarm.service

import com.dadn.be.ifarm.dto.response.PredictionResponse
import com.dadn.be.ifarm.mapper.PredictionMapper
import com.dadn.be.ifarm.repository.PredictionRepository
import com.dadn.be.ifarm.utils.Utils
import org.springframework.stereotype.Service

@Service
class PredictionService(
    private val predictionRepository : PredictionRepository,
    private val utils: Utils
) {
    private val mapper = PredictionMapper()

    fun getHumidityPrediction(steps: Int) : List<PredictionResponse> {
        val data =  predictionRepository.fetchHumidityData(steps)
        return data.map { mapper.toPredictionResponse(it, utils.getHumidDescription(it.value)) }
    }

    fun getTemperaturePrediction(steps: Int) : List<PredictionResponse> {
        val data =  predictionRepository.fetchTemperatureData(steps)
        return data.map { mapper.toPredictionResponse(it, utils.getTempDescription(it.value)) }
    }
}