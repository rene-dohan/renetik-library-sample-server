package renetik.spring.sample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

const val herokuUrl = "https://renetik-library-server.herokuapp.com"
const val localUrl = "http://localhost:8080"
const val applicationUrl = herokuUrl

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}