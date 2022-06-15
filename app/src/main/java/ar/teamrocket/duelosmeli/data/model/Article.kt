package ar.teamrocket.duelosmeli.data.model

import com.google.gson.annotations.SerializedName

data class Article (
    var id: String,
    var title: String,
    var thumbnail: String,
    var pictures: List<ArticlePicture>,
    var price: Double
)

data class ArticlePicture (
    @SerializedName("secure_url")
    var secureUrl: String
)

data class Articles (
    var results: List<Article>,
    var available_filters: List<AvailableFilters>
)

data class AvailableFilters (
    var id: String,
    var values: List<Values>
)

data class Values (
    val id: String,
    val name: String
)
