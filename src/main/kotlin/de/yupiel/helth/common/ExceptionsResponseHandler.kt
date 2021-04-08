package de.yupiel.helth.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class ExceptionsResponseHandler {

    @ExceptionHandler(
        RequestBodyException::class,
        RequestHeaderException::class,
        RequestParameterException::class,
        NotFoundException::class,
        NotAuthorizedException::class
    )
    fun requestBodyExceptionHandler(
        exception: RequestBodyException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponseDto> {
        return ResponseEntity(ExceptionResponseDto.from(exception, request), exception.statusCode)
    }

    data class ExceptionResponseDto(
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
    ) {
        companion object {
            fun from(exception: HttpClientErrorException, request: WebRequest): ExceptionResponseDto {
                return ExceptionResponseDto(
                    LocalDateTime.now(),
                    exception.rawStatusCode,
                    exception.statusCode.reasonPhrase,
                    exception.statusText,
                    request.getDescription(false).substring(4) //.replace("uri=", "")
                )
            }
        }
    }
}