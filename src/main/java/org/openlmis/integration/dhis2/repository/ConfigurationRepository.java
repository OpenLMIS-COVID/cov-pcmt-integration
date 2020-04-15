/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.integration.dhis2.repository;

import java.util.UUID;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.openlmis.integration.dhis2.domain.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

@JaversSpringDataAuditable
public interface ConfigurationRepository
    extends BaseAuditableRepository<Configuration, UUID>,
    PagingAndSortingRepository<Configuration, UUID> {

  @Query(value = "SELECT"
      + "    c.*"
      + "  FROM"
      + "    dhis2integration.configurations c"
      + "  WHERE"
      + "    id NOT IN ("
      + "      SELECT"
      + "        id"
      + "      FROM"
      + "        dhis2integration.configurations c"
      + "        INNER JOIN dhis2integration.jv_global_id g "
      + "          ON CAST(c.id AS varchar) = SUBSTRING(g.local_id, 2, 36)"
      + "        INNER JOIN dhis2integration.jv_snapshot s"
      + "          ON g.global_id_pk = s.global_id_fk"
      + "      )"
      + "  ORDER BY ?#{#pageable}",
      nativeQuery = true)
  Page<Configuration> findAllWithoutSnapshots(Pageable pageable);


}
