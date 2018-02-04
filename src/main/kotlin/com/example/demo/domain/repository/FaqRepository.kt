package com.example.demo.domain.repository

import com.example.demo.domain.models.Faq
import org.springframework.util.MultiValueMap

interface FaqRepository {
    fun findAll(): List<Faq> 
    fun findOne(id: Int): Faq
    fun saveAndFlush(faq: Faq): Faq
    fun delete(id: Int): Int
    fun findByTitleContainingAndQuestionContainingAndAnswerContaining(condition: MultiValueMap<String, String>): List<Faq>
}