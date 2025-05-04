package com.dadn.be.ifarm.service

import com.dadn.be.ifarm.dto.response.PumpResponse
import com.dadn.be.ifarm.dto.response.WaterResponse
import com.dadn.be.ifarm.dto.response.WateringStatisticResponse
import com.dadn.be.ifarm.entity.WaterData
import com.dadn.be.ifarm.repository.WaterRepository
import org.springframework.stereotype.Service
import java.time.*
import java.time.format.DateTimeFormatter

@Service
class WaterService(
    private val waterRepository: WaterRepository,
) {
    private fun combineDateTime(date : String, time : String) : String {
        val res = LocalDateTime.of(
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"))
        )
        return res.format(DateTimeFormatter.ISO_DATE_TIME).toString()
    }

    private fun duration2String(duration: Duration) : String {
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.toSeconds() % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun string2Duration(duration : String) : Duration {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = LocalTime.parse(duration, formatter)

        return Duration.between(LocalTime.MIN, time)
    }

    private fun getDuration(start : LocalDateTime, end : LocalDateTime) : String {
        val duration = Duration.between(start, end)
        return duration2String(duration)
    }

    private fun collectWateringRecords(dataList : List<WaterData>) : List<WaterResponse> {
        var resList = mutableListOf<WaterResponse>()
        if (dataList.isNotEmpty()){
            var isWorking = false
            var record : WaterResponse? = null
            var startTime: LocalDateTime? = null
            var endTime: LocalDateTime?
            for (ele in dataList){
                if (ele.value?.toInt() == 1 && !isWorking){
                    isWorking = true
                    startTime = LocalDateTime.parse(combineDateTime(ele.date!!, ele.time!!), DateTimeFormatter.ISO_DATE_TIME)
                    record = WaterResponse(time = ele.time, date = ele.date)
                } else if (ele.value?.toInt() == 0 && isWorking){
                    isWorking = false
                    endTime = LocalDateTime.of(
                        LocalDate.parse(ele.date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalTime.parse(ele.time.toString(), DateTimeFormatter.ofPattern("HH:mm:ss"))
                    )
                    record?.duration = getDuration(startTime!!, endTime)
                    resList.add(record!!)
                }
            }
        }
        return resList
    }

    fun getWateringHistory() : List<WaterResponse> {
        val dataList : List<WaterData> = waterRepository.fetchAll()
        return collectWateringRecords(dataList.reversed())
    }

    fun getWateringHistoryFilter(start : String, end : String) : List<WaterResponse> {

        val dataList : List<WaterData> = waterRepository.fetchFilter(start, end)
        return collectWateringRecords(dataList.reversed())
    }

    fun getCurrentPumpStatus() : PumpResponse {
        val data = waterRepository.fetchRecent()
        return if (data != null){
            PumpResponse(
                status = data.toInt(),
                message = "Current pump status: ${if (data.toInt() == 1) "ON"  else "OFF"}")
        }
        else PumpResponse()
    }

    fun changePumpStatus(state : Int /* 0 or 1 */) : PumpResponse {
        val success = waterRepository.changePumpState(state)
        return if (success){
            PumpResponse(
                status = state,
                message = "Set pump status to: ${if (state == 1) "ON" else "OFF"}"
            )
        } else {
            PumpResponse(message = "Failed to change pump state")
        }
    }

    fun getStatistic() : WateringStatisticResponse? {
        val end = LocalDateTime.now()
        val tempStart = end.toLocalDate().atStartOfDay()
        val start = tempStart.format(DateTimeFormatter.ISO_DATE_TIME)



        val dataList = getWateringHistoryFilter(start.toString(), end.toString())
        var duration : Duration = Duration.ZERO

        for (data in dataList){
            duration += string2Duration(data.duration!!)
        }

        val water : Double = 1.8*duration.toHours() // m3

        return if (dataList.isNotEmpty()){
            WateringStatisticResponse(
                duration = duration2String(duration),
                waterConsumption = water,
                waterTimes = dataList.size
            )
        } else {
            null
        }

    }
}
