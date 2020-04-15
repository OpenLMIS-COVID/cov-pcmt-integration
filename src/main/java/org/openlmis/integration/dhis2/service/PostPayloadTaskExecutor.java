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

package org.openlmis.integration.dhis2.service;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dhis2.postPayloadTaskExecutor")
public class PostPayloadTaskExecutor extends ThreadPoolTaskExecutor {

  @Override
  protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
    return new PriorityBlockingQueue<>(queueCapacity);
  }

  /**
   * Get the execution queue items.
   */
  public synchronized Set<PostPayloadTask> getQueueItems() {
    return getThreadPoolExecutor()
        .getQueue()
        .stream()
        .filter(runnable -> runnable instanceof PostPayloadTask)
        .map(runnable -> (PostPayloadTask) runnable)
        .collect(Collectors.toSet());
  }

}
