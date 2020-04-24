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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class PcmtLongBuilderTest {

  private static final String PCMT_GROUPING_SEPARATOR = ".";

  @InjectMocks
  private PcmtLongBuilder pcmtLongBuilder;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(pcmtLongBuilder, "groupingSeparator", PCMT_GROUPING_SEPARATOR);
  }

  @Test
  public void shouldRemoveSeparatorAndParseToLong() {
    assertEquals(Long.valueOf("10000"), pcmtLongBuilder.build("10.000"));
  }

  @Test(expected = NumberFormatException.class)
  public void shouldNotRemoveDifferentSeparatorAndThrowError() {
    pcmtLongBuilder.build("10,000");
  }

  @Test(expected = NumberFormatException.class)
  public void shouldThrowErrorWhenStringIsInvalid() {
    pcmtLongBuilder.build("10.abc");
  }

}
