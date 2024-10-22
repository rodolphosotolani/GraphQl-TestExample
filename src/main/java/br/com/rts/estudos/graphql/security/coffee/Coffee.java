package br.com.rts.estudos.graphql.security.coffee;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_COFFEES")
public class Coffee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String blendName;

    private String body;

    private String country;

    private String intensifier;

    @Enumerated(EnumType.STRING)
    private SizeCoffeeEnum size;

}
