package com.polezhaiev.avtodiva.repository.auth;

import com.polezhaiev.avtodiva.model.auth.Role;
import com.polezhaiev.avtodiva.model.auth.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}