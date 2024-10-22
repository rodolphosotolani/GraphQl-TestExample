package br.com.rts.estudos.graphql.security.coffee;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CoffeeController {

    private final CoffeeRepository repository;

    @QueryMapping
    public Optional<Coffee> coffee(@Argument Long id) {
        log.info("Find coffee by id {}!", id);
        return repository.findById(id);
    }

    @QueryMapping
    public List<Coffee> coffees() {
        log.info("Find all coffees!");
        return repository.findAll();
    }

    @MutationMapping(value = "createCoffee")
    public Coffee createCoffee(@Argument String name, @Argument SizeCoffeeEnum size) {
        return repository
                .save(Coffee
                        .builder()
                        .size(size)
                        .name(name)
                        .build());
    }

    @MutationMapping(value = "updateCoffee")
    public Coffee updateCoffee(@Argument Long id, @Argument String name, @Argument SizeCoffeeEnum size) {
        Coffee coffee = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Coffee doesn't exist"));
        return repository
                .save(coffee
                        .toBuilder()
                        .name(name)
                        .size(size)
                        .build());
    }

    @MutationMapping(value = "deleteCoffee")
    public Coffee updateCoffee(@Argument Long id) {
        Coffee coffee = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Coffee doesn't exist"));
        repository.deleteById(id);
        return coffee;
    }

}
