package com.minicommerce.commerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommerceMonolithApplication

fun main(args: Array<String>) {
    runApplication<CommerceMonolithApplication>(*args)
}
