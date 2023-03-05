package com.favorzip.muogo.repository;

import com.favorzip.muogo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserQueryRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    Optional<User> findByEmailAndProviderType(String email, String providerType);
}
