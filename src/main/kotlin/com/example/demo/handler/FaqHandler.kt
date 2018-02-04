package com.example.demo.handler

import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class FaqHandler @Autowired constructor(private val faqRepository: FaqRepository) {
    fun getFaqs(req: ServerRequest): Mono<ServerResponse> =
            ServerResponse.ok().body(Flux.fromIterable(faqRepository.findAll()))

    fun getFaq(req: ServerRequest): Mono<ServerResponse> =
            Mono.just(
                    faqRepository.findOne(req.pathVariable("id").toInt()))
                    .flatMap { v ->
                        ServerResponse.ok().body(fromObject(v))
                    }

    fun addFaq(req: ServerRequest): Mono<ServerResponse> =
            req.bodyToMono(Faq::class.java).flatMap { f ->
                ServerResponse.status(CREATED).body(fromObject(faqRepository.saveAndFlush(f)))
            }
    
    fun updateFaq(req: ServerRequest): Mono<ServerResponse> = 
            req.bodyToMono(Faq::class.java).flatMap { f -> 
                ServerResponse.status(ACCEPTED).body(fromObject(faqRepository.saveAndFlush(f)))
            }
    
    fun deleteFaq(req: ServerRequest): Mono<ServerResponse> =
            Mono.just(req.pathVariable("id")).flatMap { 
                id -> 
                faqRepository.delete(id.toInt())
                ServerResponse.status(ACCEPTED).build()
            }
    
    fun getFaqsByTitleAndQuestionAndAnswer(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(Flux.fromIterable(faqRepository.findByTitleContainingAndQuestionContainingAndAnswerContaining(req.queryParams())))
}
