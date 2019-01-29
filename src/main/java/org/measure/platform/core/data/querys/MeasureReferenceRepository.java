package org.measure.platform.core.data.querys;

import java.util.List;

import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureProperty entity.
 */
public interface MeasureReferenceRepository extends JpaRepository<MeasureReference,Long> {
    @Query(value = "select p from MeasureReference p where p.ownerInstance = :minstance")
    List<MeasureReference> findByMeasure(@Param("minstance") MeasureInstance minstance);

}
