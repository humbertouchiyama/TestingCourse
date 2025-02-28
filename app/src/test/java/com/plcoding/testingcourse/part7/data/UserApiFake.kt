package com.plcoding.testingcourse.part7.data

import com.plcoding.testingcourse.part7.domain.Post
import com.plcoding.testingcourse.part7.domain.User

class UserApiFake: UserApi {

    private val users = (1..10).map {
        User(
            id = it.toString(),
            username = "User$it"
        )
    }

    val posts = (1..10).map {
        Post(
            id = it.toString(),
            userId = it.toString(),
            title = "Title$it",
            body = "Body$it",
        )
    }

    override suspend fun getUser(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getPosts(id: String): List<Post> {
        return posts.filter { it.userId == id }
    }
}