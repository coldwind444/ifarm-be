package com.dadn.be.ifarm.controller

import com.dadn.be.ifarm.dto.response.HumidityResponse
import com.dadn.be.ifarm.service.HumidityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/humidity")
class HumidityController(
    private val humidityService: HumidityService,
) {
    @GetMapping("/current")
    fun getCurrentHumidity() : ResponseEntity<HumidityResponse>{
        val data : HumidityResponse? = humidityService.getCurrentHumidity()
        return if (data != null){
            ResponseEntity.ok().body(data)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(HumidityResponse())
        }
    }

    @GetMapping("/recent")
    fun getRecentHumidity() : ResponseEntity<String>{
        val value = humidityService.getMostRecent()
        return if (value != null){
            ResponseEntity.ok().body(value)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Humidity not found")
        }
    }

    @GetMapping
    fun getAllHumidity() : ResponseEntity<List<HumidityResponse>>{
        val dataList : List<HumidityResponse> = humidityService.getAllData()
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }

    @GetMapping("/filter")
    fun getHumidityByDateTime(
        @RequestParam start : String,
        @RequestParam end : String
    ) : ResponseEntity<List<HumidityResponse>>{
        val dataList : List<HumidityResponse> = humidityService.getFilterData(start,end)
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }
}