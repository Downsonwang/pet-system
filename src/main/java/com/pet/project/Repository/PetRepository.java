package com.pet.project.Repository;

import com.pet.project.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // 根据主人ID和宠物名查找宠物
    Optional<Pet> findByOwnerIdAndName(Long ownerId, String name);
    List<Pet> findByOwnerId(Long ownerId);

}
