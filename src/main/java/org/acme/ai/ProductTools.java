package org.acme.ai;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.persistence.Product;

@ApplicationScoped
public class ProductTools {

    @Tool("find the product")
    public Product findProductByName(String productName) {
        return Product.findByName(productName);
    }

}
