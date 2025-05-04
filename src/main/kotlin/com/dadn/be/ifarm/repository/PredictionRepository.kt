package com.dadn.be.ifarm.repository

import com.dadn.be.ifarm.api.ModelResponse
import com.dadn.be.ifarm.entity.HumidityData
import com.dadn.be.ifarm.entity.PredictedData
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient

@Repository
class PredictionRepository {
    private final val domain = "http://127.0.0.1:8000"
    private final val client = WebClient.create(domain)

    fun fetchHumidityData(steps: Int): List<PredictedData> {
        return client.get()
            .uri("/humidity/$steps")
            .retrieve()
            .bodyToFlux(ModelResponse::class.java)
            .map { it.toPredictedData() }
            .collectList()
            .block() ?: emptyList()
    }

    fun fetchTemperatureData(steps: Int): List<PredictedData> {
        return client.get()
            .uri("/temperature/$steps")
            .retrieve()
            .bodyToFlux(ModelResponse::class.java)
            .map { it.toPredictedData() }
            .collectList()
            .block() ?: emptyList()
    }
}