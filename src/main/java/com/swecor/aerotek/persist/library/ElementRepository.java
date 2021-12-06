package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.element.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends JpaRepository<Element, Integer> {

    List<Element> findByDrawingCode(String drawingCode);
}