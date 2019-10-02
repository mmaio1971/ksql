/*
 * Copyright 2019 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.execution.plan;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Immutable
public class StreamMapValues<K> implements ExecutionStep<KStreamHolder<K>> {
  private final ExecutionStepProperties properties;
  private final ExecutionStep<KStreamHolder<K>> source;
  private final List<SelectExpression> selectExpressions;

  public StreamMapValues(
      final ExecutionStepProperties properties,
      final ExecutionStep<KStreamHolder<K>> source,
      final List<SelectExpression> selectExpressions) {
    this.properties = Objects.requireNonNull(properties, "properties");
    this.source = Objects.requireNonNull(source, "source");
    this.selectExpressions = ImmutableList.copyOf(selectExpressions);
  }

  @Override
  public ExecutionStepProperties getProperties() {
    return properties;
  }

  @Override
  public List<ExecutionStep<?>> getSources() {
    return Collections.singletonList(source);
  }

  public List<SelectExpression> getSelectExpressions() {
    return selectExpressions;
  }

  public ExecutionStep<KStreamHolder<K>> getSource() {
    return source;
  }

  @Override
  public KStreamHolder<K> build(final PlanBuilder builder) {
    return builder.visitStreamMapValues(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final StreamMapValues<?> that = (StreamMapValues<?>) o;
    return Objects.equals(properties, that.properties)
        && Objects.equals(source, that.source)
        && Objects.equals(selectExpressions, that.selectExpressions);
  }

  @Override
  public int hashCode() {

    return Objects.hash(properties, source, selectExpressions);
  }
}
