package com.seproject.account.repository;

import com.seproject.account.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);

    List<Role> findByNameIn(List<String> name);

    boolean existsByName(String name);
}
