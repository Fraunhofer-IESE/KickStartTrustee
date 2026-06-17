
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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

  public final BackendProperties backendProperties;

  public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";

  private final ConcurrentHashMap<String, Collection<Long>> requestCounts = new ConcurrentHashMap<>();

  /**
   * Executed by each call of a method annotated with {@link WithRateLimitProtection} which should
   * be an HTTP endpoint.
   * Counts calls per remote address. Calls older than {@link #rateDuration} milliseconds will be
   * forgotten. If there have
   * been more than {@link #rateLimit} calls within {@link #rateDuration} milliseconds from a remote
   * address, a {@link RateLimitException}
   * will be thrown.
   * 
   * @throws RateLimitException iff rate limit for a given remote address has been exceeded
   */
  @Before("@annotation(de.fraunhofer.iese.forgetrustee.demonstrator.backend.controller.rest.WithRateLimitProtection)")
  public void rateLimit() {
    final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes();
    final String key = requestAttributes.getRequest().getRemoteAddr();
    final long currentTime = System.currentTimeMillis();
    this.requestCounts.putIfAbsent(key, new ConcurrentLinkedQueue<>());
    this.requestCounts.get(key).add(currentTime);
    this.cleanUpRequestCounts(currentTime);
    if (this.requestCounts.get(key).size() > this.backendProperties.getRateLimit()) {
      throw new RateLimitException(String
          .format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key,
              this.backendProperties.getRateDuration()));
    }
  }

  private void cleanUpRequestCounts(final long currentTime) {
    this.requestCounts.values().forEach(l -> {
      l.removeIf(t -> this.timeIsTooOld(currentTime, t));
    });
  }

  private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
    return currentTime - timeToCheck > this.backendProperties.getRateDuration();
  }
}
