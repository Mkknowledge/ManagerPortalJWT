package io.mkknowledge.managerportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.mkknowledge.managerportal.models.ERole;
import io.mkknowledge.managerportal.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);
}
