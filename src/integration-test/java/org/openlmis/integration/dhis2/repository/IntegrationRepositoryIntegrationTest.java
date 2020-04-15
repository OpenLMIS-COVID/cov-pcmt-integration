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
import org.junit.Test;
import org.openlmis.integration.dhis2.ConfigurationDataBuilder;
import org.openlmis.integration.dhis2.IntegrationDataBuilder;
import org.openlmis.integration.dhis2.domain.Configuration;
import org.openlmis.integration.dhis2.domain.Integration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

public class IntegrationRepositoryIntegrationTest
    extends BaseCrudRepositoryIntegrationTest<Integration> {

  @Autowired
  private IntegrationRepository repository;

  @Autowired
  private ConfigurationRepository configurationRepository;

  @Override
  CrudRepository<Integration, UUID> getRepository() {
    return repository;
  }

  @Override
  Integration generateInstance() {
    Configuration configuration = new ConfigurationDataBuilder().buildAsNew();
    configurationRepository.save(configuration);

    return new IntegrationDataBuilder()
        .withConfiguration(configuration)
        .buildAsNew();
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void shouldNotAllowToHaveSeveralIntegrationsWithSameTargetUrl() {
    UUID programId = UUID.randomUUID();

    repository.save(new IntegrationDataBuilder().withProgramId(programId).buildAsNew());
    repository.saveAndFlush(new IntegrationDataBuilder().withProgramId(programId).buildAsNew());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void shouldNotAllowToSaveIntegrationWithoutConfiguration() {
    repository.saveAndFlush(new IntegrationDataBuilder().withConfiguration(null).buildAsNew());
  }
}
