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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

@Service
public class ValidationMetadataService {

  private final Validator validator;

  public ValidationMetadataService(Validator validator) {
    this.validator = validator;
  }

  /**
   * Retrieves the @Size constraint for a given class and field name, checking both bean property
   * metadata and declared fields.
   *
   * @param  clazz     The class to inspect
   * @param  fieldName The name of the field/property to check
   * @return           An Optional containing the Size annotation if found, or empty if not present
   */
  public Optional<Size> getSizeConstraint(Class<?> clazz, String fieldName) {
    final BeanDescriptor bean = this.validator.getConstraintsForClass(clazz);
    final PropertyDescriptor property = bean.getConstraintsForProperty(fieldName);

    if (property != null) {
      final Optional<Size> fromMetadata = property
          .getConstraintDescriptors()
          .stream()
          .map(ConstraintDescriptor::getAnnotation)
          .filter(Size.class::isInstance)
          .map(Size.class::cast)
          .findFirst();
      if (fromMetadata.isPresent()) {
        return fromMetadata;
      }
    }

    // Fallback to checking the declared field for @Size
    return this.getFieldSizeAnnotation(clazz, fieldName);
  }

  private Optional<Size> getFieldSizeAnnotation(Class<?> clazz, String fieldName) {
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        final Field f = current.getDeclaredField(fieldName);
        final Size size = f.getAnnotation(Size.class);
        return Optional.ofNullable(size);
      } catch (final NoSuchFieldException ex) {
        current = current.getSuperclass();
      }
    }
    return Optional.empty();
  }

  public Optional<Class<?>> getPropertyType(Class<?> clazz, String fieldName) {
    try {
      final java.beans.PropertyDescriptor[] descriptors = Introspector
          .getBeanInfo(clazz)
          .getPropertyDescriptors();
      for (final java.beans.PropertyDescriptor descriptor : descriptors) {
        if (descriptor.getName().equals(fieldName)) {
          return Optional.ofNullable(descriptor.getPropertyType());
        }
      }
    } catch (final IntrospectionException ex) {
      // ignore and fallback to declared field
    }

    // Try declared field lookup
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        final Field f = current.getDeclaredField(fieldName);
        return Optional.ofNullable(f.getType());
      } catch (final NoSuchFieldException ex) {
        current = current.getSuperclass();
      }
    }
    return Optional.empty();
  }

  /**
   * Attempt to resolve the generic parameter type of a property (e.g. List<Foo> -> Foo or Map<String,Foo> -> Foo).
   * This checks the property's read method generic return type first, then falls back to the declared field's generic type.
   *
   * @param clazz     class that declares the property
   * @param fieldName property name
   * @return Optional of resolved parameter class, empty if it cannot be determined
   */
  public Optional<Class<?>> getGenericParameterType(Class<?> clazz, String fieldName) {
    // Try getter generic return type via Introspector
    try {
      final java.beans.PropertyDescriptor[] descriptors = Introspector
          .getBeanInfo(clazz)
          .getPropertyDescriptors();
      for (final java.beans.PropertyDescriptor descriptor : descriptors) {
        if (!descriptor.getName().equals(fieldName)) {
          continue;
        }
        final Method read = descriptor.getReadMethod();
        if (read != null) {
          final Optional<Class<?>> resolved = this.resolveGenericElementType(
              read.getGenericReturnType());
          if (resolved.isPresent()) {
            return resolved;
          }
        }
      }
    } catch (final IntrospectionException ignored) {
      // fallthrough to field inspection
    }

    // Fallback: inspect declared field generic type up the class hierarchy
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        final Field f = current.getDeclaredField(fieldName);
        return this.resolveGenericElementType(f.getGenericType());
      } catch (final NoSuchFieldException ex) {
        current = current.getSuperclass();
      }
    }
    return Optional.empty();
  }

  private Optional<Class<?>> resolveGenericElementType(Type type) {
    if (!(type instanceof ParameterizedType)) {
      return Optional.empty();
    }

    final ParameterizedType parameterizedType = (ParameterizedType) type;
    final Type raw = parameterizedType.getRawType();
    final Type[] args = parameterizedType.getActualTypeArguments();
    if (args == null || args.length == 0) {
      return Optional.empty();
    }

    int index = 0;
    if (raw instanceof Class && java.util.Map.class.isAssignableFrom((Class<?>) raw)
        && args.length >= 2) {
      index = 1; // Map value type
    }

    return this.resolveTypeRecursively(args[index]);
  }

  private Optional<Class<?>> resolveTypeRecursively(Type type) {
    if (type instanceof Class) {
      return Optional.of((Class<?>) type);
    }

    if (!(type instanceof ParameterizedType)) {
      return Optional.empty();
    }

    final ParameterizedType parameterizedType = (ParameterizedType) type;
    final Type raw = parameterizedType.getRawType();
    if (!(raw instanceof Class)) {
      return Optional.empty();
    }

    final Class<?> rawClass = (Class<?>) raw;
    final Type[] args = parameterizedType.getActualTypeArguments();

    if (java.util.Map.class.isAssignableFrom(rawClass) && args.length >= 2) {
      return this.resolveTypeRecursively(args[1]);
    }
    if (java.util.Collection.class.isAssignableFrom(rawClass) && args.length >= 1) {
      return this.resolveTypeRecursively(args[0]);
    }

    return Optional.of(rawClass);
  }

  /**
   * Retrieves the @Schema(requiredMode) value for a given class and field name, checking both bean
   * property accessor methods and declared fields.
   *
   * @param  clazz     The class to inspect
   * @param  fieldName The name of the field/property to check
   * @return           An Optional containing the requiredMode if found, or empty if not present
   */
  public Optional<Schema.RequiredMode> getSchemaRequiredMode(Class<?> clazz, String fieldName) {

    // Getter (Swagger schaut primär hier)
    try {
      for (final java.beans.PropertyDescriptor pd : Introspector
          .getBeanInfo(clazz)
          .getPropertyDescriptors()) {
        if (!pd.getName().equals(fieldName)) {
          continue;
        }

        final Method read = pd.getReadMethod();
        if (read != null) {
          final Schema schema = read.getAnnotation(Schema.class);
          if (schema != null && schema.requiredMode() != Schema.RequiredMode.AUTO) {
            return Optional.of(schema.requiredMode());
          }
        }
      }
    } catch (final IntrospectionException ignored) {
    }

    // Field (inkl. Superklassen)
    Class<?> current = clazz;
    while (current != null && current != Object.class) {
      try {
        final Field f = current.getDeclaredField(fieldName);
        final Schema schema = f.getAnnotation(Schema.class);
        if (schema != null && schema.requiredMode() != Schema.RequiredMode.AUTO) {
          return Optional.of(schema.requiredMode());
        }
      } catch (final NoSuchFieldException ignored) {
      }
      current = current.getSuperclass();
    }

    return Optional.empty();
  }
}
