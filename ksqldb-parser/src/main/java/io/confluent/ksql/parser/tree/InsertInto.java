/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.parser.tree;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

import com.google.errorprone.annotations.Immutable;
import io.confluent.ksql.name.SourceName;
import io.confluent.ksql.parser.NodeLocation;
import io.confluent.ksql.parser.properties.with.CreateSourceAsProperties;
import io.confluent.ksql.parser.properties.with.InsertIntoProperties;

import java.util.Objects;
import java.util.Optional;

@Immutable
public class InsertInto
    extends Statement
    implements QueryContainer {

  private final SourceName target;
  private final Query query;
  private final InsertIntoProperties properties;

  public InsertInto(
      final SourceName target,
      final Query query
  ) {
    this(Optional.empty(), target, query, InsertIntoProperties.none());
  }

  public InsertInto(
      final Optional<NodeLocation> location,
      final SourceName target,
      final Query query,
      final InsertIntoProperties properties
  ) {
    super(location);
    this.target = requireNonNull(target, "target");
    this.query = requireNonNull(query, "query");
    this.properties = requireNonNull(properties, "properties");
  }

  public SourceName getTarget() {
    return target;
  }

  public InsertIntoProperties getProperties() {
    return properties;
  }

  @Override
  public Query getQuery() {
    return query;
  }

  @Override
  public Sink getSink() {
    return Sink.of(target, false, false, CreateSourceAsProperties.none());
  }

  @Override
  public Optional<String> getQueryId() {
    return properties.getQueryId();
  }

  @Override
  public <R, C> R accept(final AstVisitor<R, C> visitor, final C context) {
    return visitor.visitInsertInto(this, context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, query, properties);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    final InsertInto o = (InsertInto) obj;
    return Objects.equals(target, o.target)
           && Objects.equals(query, o.query)
           && Objects.equals(properties, o.properties);
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("target", target)
        .add("query", query)
        .add("properties", properties)
        .toString();
  }
}

