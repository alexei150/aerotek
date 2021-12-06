package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.elementType.ElementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementTypeRepository extends JpaRepository<ElementType, Integer> {

    List<ElementType> findByName(String name);
}
