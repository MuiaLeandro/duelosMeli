package ar.teamrocket.duelosmeli.data.repository.impl

import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.model.Article
import ar.teamrocket.duelosmeli.data.model.Articles
import ar.teamrocket.duelosmeli.data.model.Category
import ar.teamrocket.duelosmeli.data.retrofit.API

class MeliRepositoryImpl : MeliRepository {

    // Se obtiene una lista de categorías
    override suspend fun searchCategories(): List<Category> {

        val response = API().getCategories()

            /*when (response.code()) {
                in 200..299 -> return response.body()!!
                400 -> throw BadRequestException(R.string.bad_request.toString())
                404 -> throw NotFoundException(R.string.resource_not_found.toString())
                in 500..599 -> throw InternalServerErrorException(R.string.server_error.toString())
                else -> throw UnknownException(R.string.unknown_error.toString())
            }*/
        return response.body()!!
    }

    // Se obtiene un item de una categoría
    override suspend fun searchItemFromCategory(id: String): Articles {

        val response = API().getArticlesFromCategory(id)

            /*when (response.code()) {
                in 200..299 -> return response.body()!!
                400 -> throw BadRequestException(R.string.bad_request.toString())
                404 -> throw NotFoundException(R.string.resource_not_found.toString())
                in 500..599 -> throw InternalServerErrorException(R.string.server_error.toString())
                else -> throw UnknownException(R.string.unknown_error.toString())
            }*/
        return response.body()!!
    }

    // Se obtienen los datos más detallados de un artículo, por ahora usamos solo una imágen
    override suspend fun searchItem(id: String): Article {

        val response = API().getArticle(id)

            /*when (response.code()) {
                in 200..299 -> return response.body()!!
                400 -> throw BadRequestException(R.string.bad_request.toString())
                404 -> throw NotFoundException(R.string.resource_not_found.toString())
                in 500..599 -> throw InternalServerErrorException(R.string.server_error.toString())
                else -> throw UnknownException(R.string.unknown_error.toString())
            }*/
        return response.body()!!
    }
}