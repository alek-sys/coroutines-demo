package com.example

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.reactive.publish
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class CoroutinesDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(CoroutinesDemoApplication::class.java, *args)
}

suspend fun sayHello(): String {
    delay(1000)
    return "Hello, world"
}

@RestController
class IndexController {

    @GetMapping("/")
    fun index() = publish(Unconfined) {
        send(sayHello())
    }
}