package com.backend.bms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
class BookManagementSystemApplication

fun main(args: Array<String>) {
	runApplication<BookManagementSystemApplication>(*args)

}
