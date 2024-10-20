package br.com.rts.estudos.graphql.security.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ROLES")
public class RoleAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Override
    public String getAuthority() {
        return this.role.name();
    }
}
