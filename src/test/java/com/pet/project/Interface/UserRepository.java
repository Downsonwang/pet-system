package com.pet.project.Interface;

import com.pet.project.Entity.UserT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserT, Long> {
}