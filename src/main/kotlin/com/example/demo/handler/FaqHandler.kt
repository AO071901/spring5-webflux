package com.example.demo.handler

import com.example.demo.domain.models.Faq
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class FaqHandler {
    val faqs = linkedMapOf<String, Faq>()
    
    fun findAll(req: ServerRequest): Mono<ServerResponse> = 
            ServerResponse.ok().body(fromObject(faqs.values))

    fun create(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(Faq::class.java).flatMap { m ->
            faqs[m.id] = m
            ServerResponse.status(HttpStatus.CREATED).body(fromObject(m))
        }
    }
}