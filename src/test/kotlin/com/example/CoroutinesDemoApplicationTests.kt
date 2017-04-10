package com.example

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class CoroutinesDemoApplicationTests {

    fun log(msg: String) = println("${Thread.currentThread().name} - $msg")

    @Test
    fun helloWorld() {
        launch(CommonPool) {
            delay(1, SECONDS)
            println("Hello, world!")
        }

        Thread.sleep(2000)
        println("Done!")
    }

    @Test
    fun helloWorld2() {
        runBlocking {
            launch(context) {
                delay(1, SECONDS)
                println("Hello, world!")
            }.join()
        }
    }

    @Test
    fun over9000() = runBlocking {
        val jobs = List(100_000) {
            launch(CommonPool) {
                print(it)
            }
        }

        jobs.forEach { it.join() }
    }

    @Ignore
    @Test
    fun over9000Threads() {
        val jobs = List(100_000) {
            thread {
                print(it)
            }
        }

        jobs.forEach(Thread::join)
    }

    suspend fun getValue(x: Int): Int {
        delay(1000L * x)
        println("Calculated result for $x")
        return x * 2
    }

    @Test
    fun `coroutines are sequential`() = runBlocking {
        val time = measureTimeMillis {
            val a = getValue(2)
            val b = getValue(1)
            log("Sum is ${a + b}")
        }

        log("done in $time ms")
    }

    @Test
    fun `but can be parallel`() = runBlocking {
        val time = measureTimeMillis {
            val a = async(CommonPool) { getValue(2) }
            val b = async(CommonPool) { getValue(1) }

            val c = a.await() + b.await()
            println(c)
        }

        log("Done in $time ms")
    }

    @Test
    fun `contexts`() = runBlocking {
        // Unconfined - run here
        launch(Unconfined) { log("Unconfined") }

        // CommonPool - use ForkJoinPool
        launch(CommonPool) { log("CommonPool") }

        // Single thread
        launch(newSingleThreadContext("single-thread")) { log("newSingleThreadContext") }

        // context - inherit context
        launch(context) { log("context") }

        delay(100)
    }

    @Test
    fun `how to communicate`() = runBlocking {
        // don't communicate by sharing memory, share memory by communicating
        val chan = Channel<Int>()

        repeat(1000) {
            launch(CommonPool) {
                for (i in chan) {
                    log(i.toString())
                }
            }
        }

        chan.send(1)
        chan.send(2)
        chan.send(3)

        delay(100)
    }
}
