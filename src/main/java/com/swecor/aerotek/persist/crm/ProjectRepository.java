package com.swecor.aerotek.persist.crm;

import com.swecor.aerotek.model.crm.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository <Project, Integer> {

    Project findByCreatedByAndId(String email, int id);

    List<Project> findByCreatedBy(String email);

    void deleteByCreatedByAndId(String email, int id);
}
