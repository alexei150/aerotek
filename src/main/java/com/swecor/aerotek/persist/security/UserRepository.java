package com.swecor.aerotek.persist.security;

import com.swecor.aerotek.model.security.EntityParent;
import com.swecor.aerotek.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("update User u set u.password =?1, u.firstName =?2, u.lastName =?3, u.patronymic =?4, u.company =?5, u.phoneNumber =?6, u.lastModifiedDate =?7 where u.email =?8")
    void updateUser(String password, String firstName, String lastName, String patronymic, String company, String phoneNumber, LocalDateTime lastModifiedDate, String email);

}
