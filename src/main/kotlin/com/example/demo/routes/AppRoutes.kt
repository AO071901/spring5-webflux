package com.example.demo.routes

import com.example.demo.handlers.MessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router


@Configuration
class AppRoutes(private val messageHandler: MessageHandler) {
    
    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/messages").nest { 
            GET("/", messageHandler::getMessages)
            POST("/", messageHandler::addMessage)
            GET("/{id}", messageHandler::getMessage)
            PUT("/{id}", messageHandler::updateMessage)
            DELETE("/{id}", messageHandler::deleteMessage)
        }
    }
}
        