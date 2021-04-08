package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class RequestParameterException(message: String): HttpClientErrorException(HttpStatus.BAD_REQUEST, message)