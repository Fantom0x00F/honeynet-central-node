package com.fantom0x00f

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class HoneyCentralNodeApplication {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(HoneyCentralNodeApplication::class.java, *args)
        }
    }
}
