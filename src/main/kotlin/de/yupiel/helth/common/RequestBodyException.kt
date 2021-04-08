package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class RequestBodyException(message:String): HttpClientErrorException(HttpStatus.BAD_REQUEST, message)