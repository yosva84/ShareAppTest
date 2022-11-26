package com.cubanengineer.test.sharefiles.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cubanengineer.test.sharefiles.models.ERole;
import com.cubanengineer.test.sharefiles.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
