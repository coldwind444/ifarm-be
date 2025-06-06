package com.dadn.be.ifarm

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class IfarmApplication

fun main(args: Array<String>) {
	runApplication<IfarmApplication>(*args)
}
