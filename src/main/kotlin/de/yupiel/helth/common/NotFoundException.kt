package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class NotFoundException(message: String): HttpClientErrorException(HttpStatus.NOT_FOUND, message)