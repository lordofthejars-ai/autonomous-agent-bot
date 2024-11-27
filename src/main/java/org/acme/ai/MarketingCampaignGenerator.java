package org.acme.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;

@RegisterAiService
public interface MarketingCampaignGenerator {

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
    String message(String message);

}
