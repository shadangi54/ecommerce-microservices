package com.shadangi54.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadangi54.auth.entity.Role;
import com.shadangi54.auth.entity.RoleName;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
}
