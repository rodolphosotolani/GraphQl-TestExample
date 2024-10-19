package br.com.rts.estudos.graphql.security.order;

import br.com.rts.estudos.graphql.security.coffee.Coffee;
import br.com.rts.estudos.graphql.security.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ORDERS")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal value;

    private LocalDateTime orderedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coffee_id")
    private Coffee coffee;
}
