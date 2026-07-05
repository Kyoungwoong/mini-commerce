package com.minicommerce.commerce.product.infrastructure

import com.minicommerce.commerce.product.domain.Product
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile("!test")
class ProductDataInitializer(
    private val productRepository: ProductRepository,
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        seedProducts
            .filterNot { productRepository.existsByName(it.name) }
            .forEach { productRepository.save(it) }
    }

    private companion object {
        val seedProducts = listOf(
            Product(name = "Keyboard", price = 30000, stockQuantity = 100),
            Product(name = "Mouse", price = 15000, stockQuantity = 120),
            Product(name = "Monitor", price = 220000, stockQuantity = 30),
            Product(name = "Laptop Stand", price = 28000, stockQuantity = 70),
            Product(name = "USB-C Hub", price = 45000, stockQuantity = 55),
            Product(name = "Desk Mat", price = 18000, stockQuantity = 90),
            Product(name = "Webcam", price = 65000, stockQuantity = 40),
            Product(name = "Microphone", price = 85000, stockQuantity = 25),
            Product(name = "Headset", price = 55000, stockQuantity = 60),
            Product(name = "Portable SSD", price = 120000, stockQuantity = 35),
        )
    }
}
