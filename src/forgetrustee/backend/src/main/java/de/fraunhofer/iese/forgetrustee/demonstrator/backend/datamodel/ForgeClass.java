
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel;
/* Created by chwalek on 02.07.2025 */

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeLiteralStatement;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.datamodel.statements.ForgeObjectStatement;

import lombok.Data;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Data
public class ForgeClass extends ForgeResource {

  String uri;

  String labelName;

  List<ForgeObjectStatement> objectStmnts = new ArrayList<>();

  List<ForgeLiteralStatement> literalStmnts = new ArrayList<>();

  public ForgeClass() {

  }

  @Override
  public String toString() {
    final StringWriter stringWriter = new StringWriter();
    stringWriter.append("//=== ").append(this.labelName).append("\n");
    stringWriter.append("URI: ").append(this.uri);
    stringWriter.append("\n").append("//=== ").append("Object Statements");
    for (final ForgeObjectStatement forgeObjectStatement : this.objectStmnts) {
      stringWriter.append("\n").append(forgeObjectStatement.toString());
    }
    stringWriter.append("\n").append("//=== ").append("Literal Statements");
    for (final ForgeLiteralStatement literalStatement : this.literalStmnts) {
      stringWriter.append("\n").append(literalStatement.toString());
    }
    stringWriter.append("\n");

    return stringWriter.toString();
  }
}
