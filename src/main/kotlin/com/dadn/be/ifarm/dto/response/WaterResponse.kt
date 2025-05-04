package com.dadn.be.ifarm.dto.response

import kotlin.time.Duration

data class WaterResponse(
    var time: String? = null,
    var date : String? = null,
    var duration: String? = null
)