package com.example.demo.domain.repository.impl

import com.example.demo.db.Tables.FAQS
import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.jooq.DSLContext
import org.jooq.impl.DSL.row
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

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
    override fun findOne(id: Int): Faq {

        val faqsRecord = dsl.selectFrom(FAQS)
                .where(FAQS.ID.eq(id))
                .fetchOne()

        return Faq(faqsRecord.id, faqsRecord.title, faqsRecord.content)
    }

    override fun saveAndFlush(faq: Faq): Faq {
        val faqId = faq.id
        when {
            faqId == 0 -> {
                val faqsRecord = dsl.newRecord(FAQS)
                with(faqsRecord) {
                    title = faq.title
                    content = faq.content
                    store()
                }
                return Faq(faqsRecord.id, faqsRecord.title, faqsRecord.content)
            }
            faqId > 0 -> {
                dsl.update(FAQS)
                        .set(
                                row(FAQS.TITLE, FAQS.CONTENT, FAQS.UPDATED_TIME),
                                row(faq.title, faq.content, Timestamp(System.currentTimeMillis()))
                        )
                        .where(FAQS.ID.eq(faqId))
                        .execute()
                return Faq(faq.id, faq.title, faq.content)
            }
            else -> return Faq()
        }
    }
}
