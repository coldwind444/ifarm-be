package com.dadn.be.ifarm.service

import com.dadn.be.ifarm.repository.HumidityRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class PumpSchedulerService(
    private val waterService: WaterService,
    private val humidityRepository: HumidityRepository
) {
    private var enabled = false

    fun enable() {
        this.enabled = true
    }

    fun disable() {
        val data = waterService.getCurrentPumpStatus()
        if (data.status == 1) waterService.changePumpStatus(0)
        this.enabled = false
    }

    @Scheduled(fixedRate = 30000)
    fun autoWatering(){
        if (!enabled) return
        val data = humidityRepository.fetchMostRecent()
        if (data != null){
            val h = data.toDouble()
            if (h < 60.0){
                waterService.changePumpStatus(1)
            } else {
                waterService.changePumpStatus(0)
            }
        }
    }
}