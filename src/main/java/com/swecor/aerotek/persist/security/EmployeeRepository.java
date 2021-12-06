package com.swecor.aerotek.persist.security;


import com.swecor.aerotek.model.security.Employee;
import com.swecor.aerotek.model.security.EntityParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    Optional<Employee> findByEmail(String email);

    void deleteByEmail(String email);

    //void showUsers(String name);

    @Modifying
    @Query("update Employee e set e.password =?1, e.firstName =?2, e.lastName =?3, e.patronymic =?4, e.company =?5, e.phoneNumber =?6, e.lastModifiedDate =?7 where e.email =?8")
    void updateEmployee(String password, String firstName, String lastName, String patronymic, String company, String phoneNumber, LocalDateTime lastModifiedDate, String email);

}
