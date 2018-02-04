package com.example.demo.routes

import com.example.demo.handler.FaqHandler
import com.example.demo.handler.MessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect
import org.springframework.web.reactive.function.server.router
import java.net.URI


@Configuration
class Routes(private val messageHandler: MessageHandler, private val faqHandler: FaqHandler) {
    
    @Bean
    fun router() = router {
        accept(TEXT_HTML).nest { 
            GET("/") { permanentRedirect(URI("index.html")).build()}
        }
        "/messages".nest {
            accept(APPLICATION_JSON).nest {
                GET("/", messageHandler::getMessages)
                POST("/", messageHandler::addMessage)
                GET("/{id}", messageHandler::getMessage)
                PUT("/{id}", messageHandler::updateMessage)
                DELETE("/{id}", messageHandler::deleteMessage)
            }
        }
        "/faq".nest { 
            accept(APPLICATION_JSON).nest { 
                POST("/", faqHandler::addFaq)
                GET("/{id}", faqHandler::getFaq)
                PUT("/{id}", faqHandler::updateFaq)
                DELETE("/{id}", faqHandler::deleteFaq)
            }
        }
        "/faqs".nest { 
            accept(APPLICATION_JSON).nest {
                GET("/", faqHandler::getFaqs)
                GET("/search", faqHandler::getFaqsByTitleAndQuestionAndAnswer)
            }
        }
        resources("/**", ClassPathResource("static/"))
    }
}
        