package ar.teamrocket.duelosmeli.model

import com.google.gson.annotations.SerializedName

data class Article (
    var id: String,
    var title: String,
    var pictures: List<ArticlePicture>,
    var price: Long
)

data class ArticlePicture (
    @SerializedName("secure_url")
    var secureUrl: String
)

data class Articles (
    var results: List<Article>
)