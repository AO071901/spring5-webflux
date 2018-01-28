package com.example.demo.handler

import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class FaqHandler @Autowired constructor(private val faqRepository: FaqRepository) {
    fun findAll(req: ServerRequest): Mono<ServerResponse> =
            ServerResponse.ok().body(Flux.fromIterable(faqRepository.findAll()))

    fun findOne(req: ServerRequest): Mono<ServerResponse> =
            Mono.just(
                    faqRepository.findOne(req.pathVariable("id").toInt()))
                    .flatMap {
                        v -> ServerResponse.ok().body(fromObject(v))
                    }
    
    fun saveAndFlush(req: ServerRequest): Mono<ServerResponse> {
        val faqId = req.pathVariable("id").toInt()
        when {
            faqId == 0 -> {
                return req.bodyToMono(Faq::class.java).flatMap { f ->
                    ServerResponse.status(HttpStatus.CREATED).body(fromObject(faqRepository.saveAndFlush(f)))
                }
            }
            faqId > 0 -> {
                return req.bodyToMono(Faq::class.java).flatMap { f ->
                    ServerResponse.status(HttpStatus.ACCEPTED).body(fromObject(faqRepository.saveAndFlush(f)))
                }
            }
        }
        return Mono.empty()
    }
}