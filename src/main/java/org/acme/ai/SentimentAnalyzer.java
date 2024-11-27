package org.acme.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface SentimentAnalyzer {

    @SystemMessage("""
            You are in charge of categorize a message sentiment to POSITIVE or NEGATIVE.
            
            The messages are done as a comment of a post in a social media platform like Twitter.
            
            Respond only with one word to describe the sentiment.
            
            Some examples are:
            
            INPUT: This is fantastic news!
            OUTPUT: POSITIVE
            
            INPUT: I like the product
            OUTPUT: POSITIVE
            
            INPUT: I really disliked the pizza. Who would use pineapples as a pizza topping?
            OUTPUT: NEGATIVE
            
            INPUT: The provided information is not useful
            OUTPUT: NEGATIVE
            
            INPUT: I miss more information about the product
            OUTPUT: NEGATIVE
            """)
    @UserMessage("""
            Analyze sentiment of the following: {comment}
            """)
    Sentiment analyze(String comment);

}
