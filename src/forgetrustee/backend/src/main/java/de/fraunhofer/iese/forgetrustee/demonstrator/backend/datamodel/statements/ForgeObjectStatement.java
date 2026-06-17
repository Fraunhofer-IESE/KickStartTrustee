
/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements;
/* Created by chwalek on 02.07.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.ForgePredicate;

import lombok.Data;

@Data
public class ForgeObjectStatement extends ForgeOntStatement {

  private String subjectUri;

  private ForgePredicate predicate;

  private String objectUri;
  private String objectLabel;

}
