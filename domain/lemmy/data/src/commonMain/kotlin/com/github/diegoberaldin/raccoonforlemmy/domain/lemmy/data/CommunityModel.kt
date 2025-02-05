package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

import kotlin.jvm.Transient

data class CommunityModel(
    val id: Int = 0,
    val instanceId: Int = 0,
    val name: String = "",
    val description: String = "",
    val title: String = "",
    val host: String = "",
    val icon: String? = null,
    val banner: String? = null,
    val subscribed: Boolean? = null,
    val instanceUrl: String = "",
    val nsfw: Boolean = false,
    val monthlyActiveUsers: Int = 0,
    val weeklyActiveUsers: Int = 0,
    val dailyActiveUsers: Int = 0,
    val subscribers: Int = 0,
    val posts: Int = 0,
    val comments: Int = 0,
    val creationDate: String? = null,
    @Transient val favorite: Boolean = false,
)

val CommunityModel.readableName: String
    get() = buildString {
        append(name)
        if (host.isNotEmpty()) {
            append("@$host")
        }
    }
