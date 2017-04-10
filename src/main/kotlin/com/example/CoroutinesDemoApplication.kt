package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class CoroutinesDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(CoroutinesDemoApplication::class.java, *args)
}

fun sayHello(): String {
    Thread.sleep(1000)
    return "Hello, world"
}

@RestController
class IndexController {

    @GetMapping("/")
    fun index(): String {
        return sayHello()
    }
}