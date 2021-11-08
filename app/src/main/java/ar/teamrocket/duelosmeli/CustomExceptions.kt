package ar.teamrocket.duelosmeli

class BadRequestException(message:String): Exception(message)
class NotFoundException(message:String): Exception(message)
class InternalServerErrorException(message:String): Exception(message)
class UnknownException(message:String): Exception(message)