package com.eb.eventsbridge.modules.auth.repository;

import com.eb.eventsbridge.modules.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
     Optional<Role> findByName(String name);
}
