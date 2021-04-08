package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.HttpClientErrorException

class RequestHeaderException(message: String): HttpClientErrorException(HttpStatus.BAD_REQUEST, message)