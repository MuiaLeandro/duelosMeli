package ar.teamrocket.duelosmeli.data.model

data class ItemDuel(
    var id: String,
    var title: String,
    var price: String,
    var image: String,
    var fakePrice: List<String>,
    var correctPosition: Int
    )
