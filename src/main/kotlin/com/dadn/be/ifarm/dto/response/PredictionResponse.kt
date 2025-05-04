package com.dadn.be.ifarm.dto.response

data class PredictionResponse(
    var dateTime: String,
    var data: Double,
    var description: String
)