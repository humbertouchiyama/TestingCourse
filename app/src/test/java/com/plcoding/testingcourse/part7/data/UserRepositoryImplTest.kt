package com.plcoding.testingcourse.part7.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var api: UserApiFake

    @BeforeEach
    fun setUp() {
        api = UserApiFake()
        repository = UserRepositoryImpl(api)
    }

    @Test
    fun `Test get profile, returns profile with user and posts`() = runBlocking {
        val resultProfile = repository.getProfile(userId = "1")

        assertThat(resultProfile).isSuccess()
        assertThat(resultProfile.getOrThrow().user.id).isEqualTo("1")

        val expectedPosts = api.posts.filter { it.userId == "1" }

        assertThat(resultProfile.getOrThrow().posts).isEqualTo(expectedPosts)
    }
}