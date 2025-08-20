package org.ahavah.portal.repositories;

import org.ahavah.portal.entities.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> getResourcesByDivision(String division);
}
