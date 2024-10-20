package br.com.rts.estudos.graphql.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {
}
