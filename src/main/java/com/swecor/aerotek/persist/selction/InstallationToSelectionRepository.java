package com.swecor.aerotek.persist.selction;

import com.swecor.aerotek.model.selection.Instalation.InstallationSelectionStatus;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstallationToSelectionRepository extends JpaRepository<InstallationToSelection, Integer> {

    List<InstallationToSelection> getByCreatedBy(String email);

    List<InstallationToSelection> getByCreatedByAndStatusNot(String email, InstallationSelectionStatus status);

    InstallationToSelection getByCreatedByAndId(String email, int id);

    void deleteByCreatedByAndId(String email, Integer id);

    @Modifying
    @Query(value = "update installation_to_selection set project_id = null " +
            "where project_id = :projectId and created_by = :email", nativeQuery = true)
    void reassignInstallations(@Param("email") String email, @Param("projectId") int projectId);
}
