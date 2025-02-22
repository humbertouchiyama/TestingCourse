package com.plcoding.testingcourse.core.domain

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.plcoding.testingcourse.core.data.ShoppingCartCacheFake
import com.plcoding.testingcourse.core.domain.Product
import com.plcoding.testingcourse.core.domain.ShoppingCart
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ShoppingCartTest {
    private lateinit var cart: ShoppingCart
    private lateinit var cacheFake: ShoppingCartCacheFake

    @BeforeEach
    fun setUp() {
        cacheFake = ShoppingCartCacheFake()
        cart = ShoppingCart(cacheFake)
    }

    @ParameterizedTest
    @CsvSource(
        "0, 0.0",
        "1, 2000.0",
        "2, 4000.0"
    )
    fun `Add multiple products, total cost sum is correct`(
        quantity: Int,
        expectedTotalCost: Double
    ) {
        cart.addProduct(Product(1, "MacBook", 2000.0), quantity)

        val totalPrice = cart.getTotalCost()
        assertThat(expectedTotalCost).isEqualTo(totalPrice)
    }

    @RepeatedTest(5)
    fun `Add product with negative quantity, throws exception`() {
        assertFailure {
            cart.addProduct(Product(1, "MacBook", 2000.0), -1)
        }
    }
}