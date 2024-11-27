package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.persistence.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ProductTest {

    @Test
    public void shouldReturnProductByName() {
        Product coolTV = Product.findByName("CoolTV");
        assertThat(coolTV.name).isEqualTo("CoolTV");
    }

}
