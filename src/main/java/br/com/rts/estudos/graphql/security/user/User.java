package br.com.rts.estudos.graphql.security.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String role;
}
