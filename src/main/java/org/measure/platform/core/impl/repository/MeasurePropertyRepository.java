package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureProperty entity.
 */
@SuppressWarnings("unused")
public interface MeasurePropertyRepository extends JpaRepository<MeasureProperty,Long> {

    @Query(value = "select p from MeasureProperty p where p.measureInstance = :minstance")
	List<MeasureProperty> findByMeasure(@Param("minstance")MeasureInstance minstance);

}
