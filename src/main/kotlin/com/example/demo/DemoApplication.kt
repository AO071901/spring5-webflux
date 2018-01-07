package com.example.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
//    runApplication<DemoApplication>(*args)
    SpringApplication.run(DemoApplication::class.java, *args)
}
