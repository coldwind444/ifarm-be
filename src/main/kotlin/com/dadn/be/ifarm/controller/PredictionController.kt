package com.dadn.be.ifarm.controller

import com.dadn.be.ifarm.dto.response.PredictionResponse
import com.dadn.be.ifarm.service.PredictionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/predictions")
class PredictionController(
    private val predictionService: PredictionService
) {
    @GetMapping("/humidity/{steps}")
    fun getHumidityPrediction(@PathVariable("steps") steps: Int) : ResponseEntity<List<PredictionResponse>> {
        val data = predictionService.getHumidityPrediction(steps)
        return ResponseEntity.ok().body(data)
    }

    @GetMapping("/temperature/{steps}")
    fun getTemperaturePrediction(@PathVariable("steps") steps: Int) : ResponseEntity<List<PredictionResponse>> {
        val data = predictionService.getTemperaturePrediction(steps)
        return ResponseEntity.ok().body(data)
    }
}