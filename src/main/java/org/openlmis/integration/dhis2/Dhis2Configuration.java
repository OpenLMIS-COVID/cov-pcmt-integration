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

package org.openlmis.integration.dhis2;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("dhis2")
public class Dhis2Configuration {

  private Measure measure = new Measure();
  private MeasureReport measureReport = new MeasureReport();

  public Set<String> getMeasureCodes() {
    return measure.mapping.keySet();
  }

  public String getMeasureMapping(String measureName) {
    return measure.mapping.get(measureName);
  }

  public String getProgramNameCodeText() {
    return measureReport.group.programNameCodeText;
  }

  public String getMeasureScoreSystem() {
    return measureReport.group.measureScoreSystem;
  }

  @Getter
  @Setter
  public static final class Measure {

    private Map<String, String> mapping = Maps.newHashMap();

  }

  @Getter
  @Setter
  public static final class MeasureReport {

    private Group group = new Group();

    @Getter
    @Setter
    public static final class Group {

      private String programNameCodeText;
      private String measureScoreSystem;

    }

  }

}
