package org.acme.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import java.util.List;
import org.acme.persistence.Reply;

@RegisterAiService
public interface MarketingCampaign {

    @SystemMessage("""
            You need to create a marketing message promoting the given product.
            The message will be used in social network like Twitter so you must be concise.
             
            The user will provide the product name and which features wants to promote from the product.
            
            The message should be at most 255 characters.
            """)
    @UserMessage("""
            
            {message}
            
            Find the product and create a message promoting it.
            The name of the product should be always in the message.
            
            Find the features value for the given product and promote only them.
            
            Be concise and add only information that you know about the product and the user expect you talk about.
            
            The message cannot contain more than 225 characters so it needs to be short and concise.
            """)
    @ToolBox(ProductTools.class)
    String generate(String message);

    @SystemMessage("""
        You need to rewrite a marketing campaign based on the original message and the comments given by users.
        
        It is important to address the concerns of the comments written 
        by users only if the comments are constructive, polite, with no harassment.
       
        
        The message should be at most 255 characters.
        """)
    @UserMessage("""
        
        Rewrite the original message: {message}
        
        
        To fix the following concerns:
        
        {#for reply in replies}
        - {reply.message}
        {/for}
        
        
        Find the product in the original message and create a message fixing the concerns.
        The name of the product should be always in the message.

        Find the features value for the given product and promote only them.

        Be concise and add only information that you know about the product and the user expect you talk about.
        
        The message cannot contain more than 225 characters so it needs to be short and concise.
        
        """)
    @ToolBox(ProductTools.class)
    String recreate(String message, List<Reply> replies);

    @SystemMessage("""
            You need to recreate a successful marketing message based on the original message and all replies from the viewers of the campign. 
            
            The message should be at most 255 characters.
    """)
    @UserMessage("""
        
        Rewrite the original message: {message}
        
        Summarizing the following positive comments:
        
        {#for reply in replies}
        - {reply.message}
        {/for}
        
        The message cannot contain more than 225 characters so it needs to be short and concise.
        """)
    String rebump(String message, List<Reply> replies);

}
