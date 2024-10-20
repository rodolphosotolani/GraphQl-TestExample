package br.com.rts.estudos.graphql.security;

import br.com.rts.estudos.graphql.security.coffee.Coffee;
import br.com.rts.estudos.graphql.security.coffee.CoffeeRepository;
import br.com.rts.estudos.graphql.security.coffee.SizeCoffeeEnum;
import br.com.rts.estudos.graphql.security.order.Order;
import br.com.rts.estudos.graphql.security.order.OrderRepository;
import br.com.rts.estudos.graphql.security.user.*;
import graphql.com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
@RequiredArgsConstructor
public class DataLoaderCommandLineRunner implements CommandLineRunner {

    public static final int COUNT_ORDERS = 100;
    public static final int COUNT_COFFEES = 10;

    private final CoffeeRepository coffeeRepository;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final RoleAuthorityRepository roleAuthorityRepository;

    private final Faker faker;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        List<Coffee> coffees = this.createCoffees();
        List<User> users = this.createUsers();
        this.createOrders(coffees, users);

    }

    private List<User> createUsers() {
        var users = new ArrayList<User>();

        var user = this.createUser(RoleEnum.ROLE_USER);
        var admin = this.createUser(RoleEnum.ROLE_ADMIN);

        users.add(User
                .builder()
                .name("Rodolpho T. Sotolani")
                .login("user")
                .password(passwordEncoder.encode("password"))
                .grantedAuthorities(Collections.singleton(user))
                .build());

        users.add(User
                .builder()
                .name("Cristine de Almeida Pilger")
                .login("admin")
                .password(passwordEncoder.encode("password"))
                .grantedAuthorities(Sets.newHashSet(user, admin))
                .build());

        return userRepository.saveAll(users);
    }

    private RoleAuthority createUser(RoleEnum roleEnum) {
        return roleAuthorityRepository
                .save(RoleAuthority
                        .builder()
                        .role(roleEnum)
                        .build());
    }

    private void createOrders(List<Coffee> coffees, List<User> users) {

        log.info("Creating the Orders Coffee List in dataBase");

        for (int count = 0; count < COUNT_ORDERS; count++) {
            Coffee coffee = coffees.get(faker.number().numberBetween(0, coffees.size()));
            User user = users.get(faker.number().numberBetween(0, users.size()));
            var orderedOn = LocalDateTime.ofInstant(
                    faker.timeAndDate().past(faker.number().randomNumber(3, true), TimeUnit.HOURS), ZoneId.systemDefault());

            orderRepository
                    .save(Order
                            .builder()
                            .coffee(coffee)
                            .value(new BigDecimal(faker.number().randomNumber()))
                            .user(user)
                            .orderedOn(orderedOn)
                            .build());
        }
    }

    private List<Coffee> createCoffees() {

        log.info("Creating the Events List in dataBase");

        var coffees = new ArrayList<Coffee>();
        for (int count = 0; count < COUNT_COFFEES; count++) {


            coffees.add(Coffee
                    .builder()
                    .name(faker.coffee().name1())
                    .body(faker.coffee().body())
                    .country(faker.coffee().country())
                    .blendName(faker.coffee().blendName())
                    .intensifier(faker.coffee().intensifier())
                    .size(SizeCoffeeEnum.values()[faker.number().numberBetween(0, SizeCoffeeEnum.values().length)])
                    .build());
        }

        log.info("Saving the {} coffees...", coffees.size());
        return coffeeRepository.saveAll(coffees);
    }

}
