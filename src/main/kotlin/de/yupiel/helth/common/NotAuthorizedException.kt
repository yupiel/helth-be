package de.yupiel.helth.common

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotAuthorizedException(message: String): RuntimeException(message)