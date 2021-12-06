package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.builder.DefaultBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultBuilderRepository extends JpaRepository <DefaultBuilder, Integer> {
}
