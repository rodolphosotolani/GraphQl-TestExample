package br.com.rts.estudos.graphql.security.coffee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.*;

@Import(CoffeeRepository.class)
@GraphQlTest(CoffeeController.class)
class CoffeeControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void testFindAllCoffeeShouldReturnAllCoffees() {

        //language=GraphQL
        String document = """
                query {
                coffees {
                    id,
                    name,
                    size
                    }
                }
                """;

        graphQlTester.document(document)
                .execute()
                .path("coffees")
                .entityList(Coffee.class)
                .hasSize(10);
    }

    @Test
    void validIdShoudReturnCoffee() {
        //language=GraphQL
        String document = """
                query findCoffee($id: ID!){
                coffee(id: $id) {
                    id,
                    name,
                    size
                    }
                }
                """;

        graphQlTester.document(document)
                .variable("id", 1)
                .execute()
                .path("coffee")
                .entity(Coffee.class)
                .satisfies(coffee -> {
                    assertNotNull(coffee);
                    assertEquals(1, coffee.getId());
                });
    }

    @Test
    void shouldCreateNewCoffee() {
        //language=GraphQL
        String document = """
                mutation createCoffee($name: String!, $size: SizeCoffee!){
                createCoffee(name: $name, size: $size) {
                    id,
                    name,
                    size
                    }
                }
                """;

        graphQlTester.document(document)
                .variable("name", "Coffee Latte")
                .variable("size", SizeCoffeeEnum.TAll)
                .execute()
                .path("createCoffee")
                .entity(Coffee.class)
                .satisfies(coffee -> {
                    assertEquals("Coffee Latte", coffee.getName());
                    assertEquals(SizeCoffeeEnum.TAll, coffee.getSize());
                });
    }

    @Test
    void shouldUpdateCoffee() {
        //language=GraphQL
        String document = """
                mutation updateCoffee($id: ID!, $name: String!, $size: SizeCoffee!){
                updateCoffee(id: $id, name: $name, size: $size) {
                    id,
                    name,
                    size
                    }
                }
                """;

        graphQlTester.document(document)
                .variable("id", 1)
                .variable("name", "UPDATED: Coffee Latte")
                .variable("size", SizeCoffeeEnum.VENTI)
                .execute()
                .path("updateCoffee")
                .entity(Coffee.class)
                .satisfies(coffee -> {
                    assertNotEquals("Coffee Latte", coffee.getName());
                    assertEquals("UPDATED: Coffee Latte", coffee.getName());
                    assertNotEquals(SizeCoffeeEnum.TAll, coffee.getSize());
                    assertEquals(SizeCoffeeEnum.VENTI, coffee.getSize());
                });
    }

    @Test
    void shouldDeleteCoffee() {
        //language=GraphQL
        String document = """
                mutation deleteCoffee($id: ID!){
                deleteCoffee(id: $id) {
                    id,
                    name,
                    size
                    }
                }
                """;

        graphQlTester.document(document)
                .variable("id", 1)
                .execute()
                .path("deleteCoffee")
                .entity(Coffee.class)
                .satisfies(coffee -> {
                    assertNotEquals("Coffee Latte", coffee.getName());
                    assertEquals("UPDATED: Coffee Latte", coffee.getName());
                    assertNotEquals(SizeCoffeeEnum.TAll, coffee.getSize());
                    assertEquals(SizeCoffeeEnum.VENTI, coffee.getSize());
                });
    }

}