package com.dadn.be.ifarm.controller

import com.dadn.be.ifarm.dto.response.PumpResponse
import com.dadn.be.ifarm.dto.response.WaterResponse
import com.dadn.be.ifarm.dto.response.WateringStatisticResponse
import com.dadn.be.ifarm.repository.WaterRepository
import com.dadn.be.ifarm.service.PumpSchedulerService
import com.dadn.be.ifarm.service.WaterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/water")
class WaterController(
    private val waterService: WaterService,
    private val scheduleService: PumpSchedulerService,
    private val waterRepository: WaterRepository
) {
    @GetMapping
    fun getWateringHistory() : ResponseEntity<List<WaterResponse>> {
        val dataList : List<WaterResponse> = waterService.getWateringHistory()
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }

    @GetMapping("/filter")
    fun getWateringHistoryByDateTime(
        @RequestParam start : String,
        @RequestParam end : String
    ) : ResponseEntity<List<WaterResponse>> {
        val dataList = waterService.getWateringHistoryFilter(start, end)
        return if (dataList.isNotEmpty()){
            ResponseEntity.ok().body(dataList)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
        }
    }

    @GetMapping("/pump")
    fun getCurrentPumpState() : ResponseEntity<PumpResponse> {
        val data = waterService.getCurrentPumpStatus()
        return if (data.status != -1){
            ResponseEntity.ok().body(data)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PostMapping("/pump/{state}")
    fun changePumpState(@PathVariable state : Int) : ResponseEntity<PumpResponse> {
        val response = waterService.changePumpStatus(state)
        return if (response.status != -1){
            ResponseEntity.ok().body(response)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }
    }

    @PostMapping("/pump/auto/enable")
    fun enableAutoMode() : ResponseEntity<String> {
        scheduleService.enable()
        return ResponseEntity.ok().body("Auto mode is enabled")
    }

    @PostMapping("/pump/auto/disable")
    fun disableAutoMode() : ResponseEntity<String> {
        scheduleService.disable()
        return ResponseEntity.ok().body("Auto mode is disabled")
    }

    @GetMapping("/statistics")
    fun getWateringStatistics() : ResponseEntity<WateringStatisticResponse> {
        val data = waterService.getStatistic()
        return if (data != null){
            ResponseEntity.ok().body(data)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }
}