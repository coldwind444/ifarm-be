package com.dadn.be.ifarm.repository

import com.dadn.be.ifarm.api.AdafruitResponse
import com.dadn.be.ifarm.dto.request.ChangePumpStateRequest
import com.dadn.be.ifarm.entity.WaterData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Repository
class WaterRepository {
    @Value("\${api.secret.key}")
    private lateinit var key : String

    private final val domain = "https://io.adafruit.com/api/v2/huynhat/feeds"
    private final val client = WebClient.create(domain)

    private fun gmt7plus2Zulu(input : String) : String {
        val gmt7plus = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
        val zuluTime = gmt7plus.minusHours(7)

        return zuluTime.format(DateTimeFormatter.ISO_DATE_TIME).toString()
    }


    fun fetchAll() : List<WaterData>{
        return client.get()
            .uri("/v3/data")
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map{ it.toWater() }
            .collectList()
            .block() ?: emptyList()
    }

    fun fetchLast() : WaterData? {
        return client.get()
            .uri("/v3/data/last")
            .retrieve()
            .bodyToMono(AdafruitResponse::class.java)
            .map { it.toWater() }
            .block()
    }

    fun fetchRecent() : String? {
        return client.get()
            .uri("/v3/data/retain")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()?.substringBefore(",")
    }

    fun fetchFilter(start : String, end : String) : List<WaterData>{
        val startZulu = gmt7plus2Zulu(start)
        val endZulu = gmt7plus2Zulu(end)
        val uri : String = "/v3/data?start_time=$startZulu&end_time=$endZulu"
        return client.get()
            .uri(uri)
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map { it.toWater() }
            .collectList()
            .block() ?: emptyList()
    }

    fun changePumpState(state : Int) : Boolean {
        try {
            client.post().uri("/v3/data").header("X-AIO-Key", key)
                .bodyValue(ChangePumpStateRequest(value = state.toString()))
                .retrieve()
                .bodyToMono(AdafruitResponse::class.java)
                .block()
            return true
        } catch (e: Exception) {
            return false
        }
    }

}