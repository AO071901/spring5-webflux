package com.example.demo.domain.repository

import com.example.demo.domain.models.Faq

interface FaqRepository {
    fun findAll(): Map<String, Faq> 
//    fun create(faq: Mono<Faq>): Mono<Void>
}