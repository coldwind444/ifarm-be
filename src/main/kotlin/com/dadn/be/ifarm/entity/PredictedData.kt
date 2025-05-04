package com.dadn.be.ifarm.entity

import java.time.LocalDateTime

data class PredictedData(
    var dateTime: LocalDateTime,
    var value: Double
)
