package com.migferlab.justpizza.domain.model

data class User(val uid: String, val favs: List<String> = listOf()) {
}