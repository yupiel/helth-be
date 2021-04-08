package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class NotAuthorizedException(message: String): HttpClientErrorException(HttpStatus.FORBIDDEN, message)