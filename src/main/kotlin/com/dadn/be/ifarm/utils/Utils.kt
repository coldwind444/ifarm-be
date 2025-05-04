package com.dadn.be.ifarm.utils

import org.springframework.stereotype.Component

@Component
class Utils {
    fun getHumidDescription(data : Double) : String {
        if (data != -1.0){
            return if (data < 30){
                "Too dry"
            } else if (data in 30.0..59.0){
                "Medium"
            } else if (data in 60.0..79.0){
                "Good"
            } else {
                "Too wet"
            }
        }
        return ""
    }

    fun getTempDescription(data : Double) : String {
        if (data != -1.0){
            return if (data < 12){
                "Too cold"
            } else if (data in 12.0..17.0 || data in 25.0..30.0){
                "Medium"
            } else if (data in 18.0 .. 24.0){
                "Good"
            } else {
                "Too hot"
            }
        }
        return ""
    }
}