package org.measure.platform.core.impl.repository;

import java.time.LocalDate;
import java.util.List;

import org.measure.platform.framework.domain.PersistentToken;
import org.measure.platform.framework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
