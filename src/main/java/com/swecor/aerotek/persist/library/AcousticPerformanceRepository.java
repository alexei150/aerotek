package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.section.AcousticPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcousticPerformanceRepository extends JpaRepository<AcousticPerformance, Integer> {
}
