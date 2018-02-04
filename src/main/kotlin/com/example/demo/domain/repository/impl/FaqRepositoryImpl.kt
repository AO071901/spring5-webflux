package com.example.demo.domain.repository.impl

import com.example.demo.db.Tables.FAQ
import com.example.demo.db.tables.records.FaqRecord
import com.example.demo.domain.models.Faq
import com.example.demo.domain.repository.FaqRepository
import org.jooq.DSLContext
import org.jooq.Operator
import org.jooq.SelectQuery
import org.jooq.TableField
import org.jooq.impl.DSL.row
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import java.sql.Timestamp

@Repository
class FaqRepositoryImpl @Autowired constructor(private var dsl: DSLContext) : FaqRepository {
    @Transactional
    override fun findAll(): List<Faq> {
        val faqs: MutableList<Faq> = mutableListOf()
        dsl.selectFrom(FAQ).fetch()
                .forEach {
                    faqs.add(Faq(it.faqId, it.title, it.question, it.answer))
                }

        return faqs
    }

    @Transactional
    override fun findOne(id: Int): Faq {

        val faqRecord = dsl.selectFrom(FAQ)
                .where(FAQ.FAQ_ID.eq(id))
                .fetchOne()

        return Faq(faqRecord.faqId, faqRecord.title, faqRecord.question, faqRecord.answer)
    }

    override fun saveAndFlush(faq: Faq): Faq {
        val faqId = faq.faq_id
        when {
            faqId == 0 -> {
                val faqRecord = dsl.newRecord(FAQ)
                with(faqRecord) {
                    title = faq.title
                    question = faq.question
                    answer = faq.answer
                    store()
                }
                return Faq(faqRecord.faqId, faqRecord.title, faqRecord.question, faqRecord.answer)
            }
            faqId > 0 -> {
                dsl.update(FAQ)
                        .set(
                                row(FAQ.TITLE, FAQ.QUESTION, FAQ.ANSWER, FAQ.UPDATED_TIME),
                                row(faq.title, faq.question, faq.answer, Timestamp(System.currentTimeMillis()))
                        )
                        .where(FAQ.FAQ_ID.eq(faqId))
                        .execute()
                return Faq(faq.faq_id, faq.title, faq.question, faq.answer)
            }
            else -> return Faq()
        }
    }

    override fun delete(id: Int): Int =
            dsl.deleteFrom(FAQ).where(FAQ.FAQ_ID.eq(id)).execute()

    override fun findByTitleContainingAndQuestionContainingAndAnswerContaining(condition: MultiValueMap<String, String>): List<Faq> {
        val title = condition.get("title")
        val question = condition.get("question")
        val answer = condition.get("answer")

        val selectQuery = dsl.selectFrom(FAQ).query
        condition.forEach { it ->
            run {
                var field = it.key
                when {
                    field.equals("title") -> {
                        addConditions(it, FAQ.TITLE, selectQuery)
                    }
                    field.equals("question") -> {
                        addConditions(it, FAQ.QUESTION, selectQuery)
                    }
                    field.equals("answer") -> {
                        addConditions(it, FAQ.ANSWER, selectQuery)
                    }
                }
            }
        }

        val faqs: MutableList<Faq> = mutableListOf()

        println(selectQuery.sql)
        println(selectQuery.toString())

        selectQuery
                .fetch()
                .forEach {
                    faqs.add(Faq(it.faqId, it.title, it.question, it.answer))
                }

        return faqs
    }

    private fun addConditions(map: Map.Entry<String, MutableList<String>>, field: TableField<FaqRecord, String>, selectQuery: SelectQuery<FaqRecord>) {
        map.value.forEach { it ->
            selectQuery.addConditions(Operator.OR, field.like("%$it%"))
        }
    }
}