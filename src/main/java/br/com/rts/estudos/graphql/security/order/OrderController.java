package br.com.rts.estudos.graphql.security.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;

    @QueryMapping
    public List<Order> orders(Principal principal){
        return repository.findAllByUserLogin(principal.getName());
    }
}
