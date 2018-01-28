package com.example.demo.domain.repository

import com.example.demo.domain.models.Faq

interface FaqRepository {
    fun findAll(): List<Faq> 
    fun findById(id: Int): Faq
    fun saveAndFlush(faq: Faq): Faq
}