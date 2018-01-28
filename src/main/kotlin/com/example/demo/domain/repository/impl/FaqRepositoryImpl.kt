package com.example.demo.domain.repository.impl

import com.example.demo.db.Tables.FAQS
import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class FaqRepositoryImpl @Autowired constructor(private var dsl: DSLContext) : FaqRepository {
    @Transactional
    override fun findAll(): List<Faq> {
        val faqs: MutableList<Faq> = mutableListOf()
        dsl.selectFrom(FAQS).fetch()
                .forEach {
                    faqs.add(Faq(it.id, it.title, it.content))
                }

        return faqs
    }

    @Transactional
    override fun findById(id: Int): Faq {

        val faqsRecord = dsl.selectFrom(FAQS)
                .where(FAQS.ID.eq(id))
                .fetchOne()

        return Faq(faqsRecord.id, faqsRecord.title, faqsRecord.content)
    }

    override fun saveAndFlush(faq: Faq): Faq {
        val faqsRecord = dsl.newRecord(FAQS)
        faqsRecord.id = faq.id
        faqsRecord.title = faq.title
        faqsRecord.content = faq.content
        faqsRecord.store()
        return Faq(faqsRecord.id, faqsRecord.title, faqsRecord.content)
    }
}