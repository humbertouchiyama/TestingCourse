package com.plcoding.testingcourse.part6

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {

    private lateinit var orderService: OrderService
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailClient: EmailClient

    @BeforeEach
    fun setUp() {
        val firebaseUser = mockk<FirebaseUser>(relaxed = true)
        firebaseAuth = mockk(relaxed = true) {
            every { currentUser } returns firebaseUser
        }
        emailClient = mockk(relaxed = true)
        orderService = OrderService(
            auth = firebaseAuth,
            emailClient = emailClient
        )
    }

    @Test
    fun `Test order is placed when user is not anonymous`() {
        every { firebaseAuth.currentUser?.isAnonymous } returns false

        orderService.placeOrder("test email", "Pan")

        verify {
            emailClient.send(
                Email(
                    subject = "Order Confirmation",
                    content ="Thank you for your order of Pan.",
                    recipient = "test email"
                )
            )
        }
    }

    @Test
    fun `Test order is not placed when user is anonymous`() {
        every { firebaseAuth.currentUser?.isAnonymous } returns true

        orderService.placeOrder("test email", "bottle")

        verify(exactly = 0) {
            emailClient.send(
                Email(
                    subject = "Order Confirmation",
                    content ="Thank you for your order of bottle.",
                    recipient = "test email"
                )
            )
        }
    }
}