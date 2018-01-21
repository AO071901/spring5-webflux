package com.example.demo.domain.models

import com.example.demo.db.tables.records.FaqsRecord

data class Faq(private var faqsRecord: FaqsRecord) {
    val id: Int = faqsRecord.id
    val title: String = faqsRecord.title
    val content: String = faqsRecord.content
}