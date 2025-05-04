package com.dadn.be.ifarm.repository

import com.dadn.be.ifarm.api.AdafruitResponse
import com.dadn.be.ifarm.entity.TemperatureData
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Dictionary

@Repository
class TemperatureRepository {
    private final val domain = "https://io.adafruit.com/api/v2/huynhat/feeds"
    private final val client = WebClient.create(domain)

    private fun gmt7plus2Zulu(input : String) : String {
        val gmt7plus = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
        val zuluTime = gmt7plus.minusHours(7)

        return zuluTime.format(DateTimeFormatter.ISO_DATE_TIME).toString()
    }

    fun fetchAll() : List<TemperatureData>{
        return client.get()
            .uri("/v1/data")
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map{ it.toTemperature() }
            .collectList()
            .block() ?: emptyList()
    }

    fun fetchLast() : TemperatureData? {
        return client.get()
            .uri("/v1/data/last")
            .retrieve()
            .bodyToMono(AdafruitResponse::class.java)
            .map { it.toTemperature() }
            .block()
    }

    fun fetchMostRecent() : String? {
        val csv =  client.get()
            .uri("/v1/data/retain")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return if (csv != null){
            csv.substringBefore(",")
        } else
            return null
    }

    fun fetchFilter(start : String, end : String) : List<TemperatureData>{
        val startZulu = gmt7plus2Zulu(start)
        val endZulu = gmt7plus2Zulu(end)
        val uri : String = "/v1/data?start_time=$startZulu&end_time=$endZulu"
        return client.get()
            .uri(uri)
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map { it.toTemperature() }
            .collectList()
            .block() ?: emptyList()
    }
}