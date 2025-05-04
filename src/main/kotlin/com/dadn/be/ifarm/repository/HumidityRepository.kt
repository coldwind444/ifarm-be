package com.dadn.be.ifarm.repository

import com.dadn.be.ifarm.api.AdafruitResponse
import com.dadn.be.ifarm.entity.HumidityData
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Dictionary

@Repository
class HumidityRepository {
    private final val domain = "https://io.adafruit.com/api/v2/huynhat/feeds"
    private final val client = WebClient.create(domain)

    private fun gmt7plus2Zulu(input : String) : String {
        val gmt7plus = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
        val zuluTime = gmt7plus.minusHours(7)

        return zuluTime.format(DateTimeFormatter.ISO_DATE_TIME).toString()
    }


    fun fetchAll() : List<HumidityData>{
        return client.get()
            .uri("/v2/data")
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map{ it.toHumidity() }
            .collectList()
            .block() ?: emptyList()
    }

    fun fetchLast() : HumidityData? {
        return client.get()
            .uri("/v2/data/last")
            .retrieve()
            .bodyToMono(AdafruitResponse::class.java)
            .map { it.toHumidity() }
            .block()
    }

    fun fetchMostRecent() : String? {
        val csv =  client.get()
            .uri("/v2/data/retain")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return if (csv != null){
            csv.substringBefore(",")
        } else
            return null
    }

    fun fetchFilter(start : String, end : String) : List<HumidityData>{
        val startZulu = gmt7plus2Zulu(start)
        val endZulu = gmt7plus2Zulu(end)
        val uri : String = "/v2/data?start_time=$startZulu&end_time=$endZulu"
        return client.get()
            .uri(uri)
            .retrieve()
            .bodyToFlux(AdafruitResponse::class.java)
            .map { it.toHumidity() }
            .collectList()
            .block() ?: emptyList()
    }
}