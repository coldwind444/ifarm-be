package com.dadn.be.ifarm.controller

import com.dadn.be.ifarm.dto.response.TemperatureResponse
import com.dadn.be.ifarm.service.TemperatureService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/temperature")
class TemperatureController(
    private val temperatureService: TemperatureService,
) {
    @GetMapping("/current")
    fun getCurrentTemperature() : ResponseEntity<TemperatureResponse> {
        val data : TemperatureResponse? = temperatureService.getCurrentTemperature()
        return if (data != null){
            ResponseEntity.ok().body(data)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(TemperatureResponse())
        }
    }

    @GetMapping("/recent")
    fun getRecentTemperature() : ResponseEntity<String> {
        val data = temperatureService.getRecentTemperature()
        return if (data != null){
            ResponseEntity.ok().body(data)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @GetMapping
    fun getAllTemperature() : ResponseEntity<List<TemperatureResponse>> {
        val dataList : List<TemperatureResponse> = temperatureService.getAllData()
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }

    @GetMapping("/filter")
    fun getTemperatureByDateTime(
        @RequestParam start : String,
        @RequestParam end : String
    ) : ResponseEntity<List<TemperatureResponse>> {
        val dataList : List<TemperatureResponse> = temperatureService.getFilterData(start,end)
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }
}