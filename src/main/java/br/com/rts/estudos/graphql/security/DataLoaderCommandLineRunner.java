package br.com.rts.estudos.graphql.security;

import br.com.rts.estudos.graphql.security.coffee.Coffee;
import br.com.rts.estudos.graphql.security.coffee.CoffeeRepository;
import br.com.rts.estudos.graphql.security.coffee.SizeCoffeeEnum;
import br.com.rts.estudos.graphql.security.order.Order;
import br.com.rts.estudos.graphql.security.order.OrderRepository;
import br.com.rts.estudos.graphql.security.user.User;
import br.com.rts.estudos.graphql.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    private final Faker faker;

    @Override
    public void run(String... args) throws Exception {

        List<Coffee> coffees = this.createCoffees();
        List<User> users = this.createUsers();
        this.createOrders(coffees, users);

    }

    private List<User> createUsers() {
        var users = new ArrayList<User>();

        users.add(User
                .builder()
                .name("User")
                .role("USER")
                .build());

        users.add(User
                .builder()
                .name("Admin")
                .role("USER, ADMIN")
                .build());

        return userRepository.saveAll(users);
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
                    .name(faker.marvelSnap().event())
                    .size(SizeCoffeeEnum.values()[faker.number().numberBetween(0, SizeCoffeeEnum.values().length)])
                    .build());
        }

        log.info("Saving the {} coffees...", coffees.size());
        return coffeeRepository.saveAll(coffees);
    }

}
