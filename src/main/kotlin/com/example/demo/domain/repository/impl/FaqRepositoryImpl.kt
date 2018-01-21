package com.example.demo.domain.repository.impl

import com.example.demo.db.Tables.FAQS
import com.example.demo.db.tables.records.FaqsRecord
import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class FaqRepositoryImpl @Autowired constructor(private var dsl : DSLContext): FaqRepository {
    @Transactional
    override fun findAll(): Map<String, Faq> {
        val faqs: MutableList<Faq> = mutableListOf()
        var result: Result<Record> = dsl.select().from(FAQS).fetch()
        result.forEach {
            faqs.add(Faq(it.into(FaqsRecord::class.java)))
        }
        return faqs.associateBy({it.title}, {it})
    } 
    
//    override fun create(faq: Mono<Faq>): Mono<Void> {
//        return Faq("id", "title", "content")
//    }
}