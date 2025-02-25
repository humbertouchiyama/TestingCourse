package com.plcoding.testingcourse.core.data

import assertk.assertThat
import assertk.assertions.isFailure
import com.plcoding.testingcourse.core.domain.AnalyticsLogger
import com.plcoding.testingcourse.core.domain.LogParam
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

internal class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi
    private lateinit var analyticsLogger: AnalyticsLogger

    @BeforeEach
    fun setUp() {
        productApi = mockk()
        analyticsLogger = mockk(relaxed = true)
        repository = ProductRepositoryImpl(
            productApi = productApi,
            analyticsLogger = analyticsLogger
        )
    }

    @Test
    fun `Test when response error on purchase products, exception is logged`() = runBlocking {
        every { analyticsLogger.logEvent(any()) } answers {
            println("This is a mocked log event")
        }
        coEvery { productApi.purchaseProducts(any()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test error message"
        }

        val result = repository.purchaseProducts(listOf())

        assertThat(result).isFailure()

        verify(exactly = 1) {
            analyticsLogger.logEvent(
                "http_error",
                LogParam("code", 404),
                LogParam("message", "Test error message")
            )
        }
    }
}