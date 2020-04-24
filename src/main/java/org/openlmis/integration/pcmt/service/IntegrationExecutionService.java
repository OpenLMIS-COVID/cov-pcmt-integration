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

package org.openlmis.integration.pcmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.openlmis.integration.pcmt.domain.Integration;
import org.openlmis.integration.pcmt.repository.ExecutionRepository;
import org.openlmis.integration.pcmt.service.auth.AuthService;
import org.openlmis.integration.pcmt.service.fetch.IntegrationFetchExecutor;
import org.openlmis.integration.pcmt.service.fetch.OrderableIntegrationFetchTask;
import org.openlmis.integration.pcmt.service.pcmt.PcmtDataService;
import org.openlmis.integration.pcmt.service.referencedata.orderable.OrderableDto;
import org.openlmis.integration.pcmt.service.send.IntegrationSendExecutor;
import org.openlmis.integration.pcmt.service.send.IntegrationSendTask;
import org.openlmis.integration.pcmt.service.send.OrderableIntegrationSendTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationExecutionService {

  @Autowired
  private Clock clock;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ExecutionRepository executionRepository;

  @Autowired
  private IntegrationSendExecutor integrationSendExecutor;

  @Autowired
  private IntegrationFetchExecutor integrationFetchExecutor;

  @Autowired
  private AuthService authService;

  @Autowired
  private PcmtDataService pcmtDataService;

  @Autowired
  private PcmtLongBuilder pcmtLongBuilder;

  private final BlockingQueue<OrderableDto> queue = new LinkedBlockingDeque<>();

  /**
   * Method is responsible for sending payload to Interop layer. Response is a status (202, 500 or
   * 503), message and notificationsChannel.
   */
  public void integrate(UUID userId, Integration integration, boolean manualExecution) {
    OrderableIntegrationFetchTask producer = new OrderableIntegrationFetchTask(pcmtDataService,
        pcmtLongBuilder, queue, clock);
    IntegrationSendTask<OrderableDto> consumer = new OrderableIntegrationSendTask(
        queue, integration, userId, manualExecution,
        executionRepository, clock, objectMapper, authService);

    integrationFetchExecutor.execute(producer);
    integrationSendExecutor.execute(consumer);
  }

}
