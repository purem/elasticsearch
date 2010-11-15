/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.mapper.xcontent;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.mapper.MergeMappingException;

import java.io.IOException;

/**
 * @author kimchy (shay.banon)
 */
public class RoutingFieldMapper extends AbstractFieldMapper<String> implements org.elasticsearch.index.mapper.RoutingFieldMapper {

    public static final String CONTENT_TYPE = "_routing";

    public static class Defaults extends AbstractFieldMapper.Defaults {
        public static final String NAME = "_routing";
        public static final Field.Index INDEX = Field.Index.NOT_ANALYZED;
        public static final Field.Store STORE = Field.Store.YES;
        public static final boolean OMIT_NORMS = true;
        public static final boolean OMIT_TERM_FREQ_AND_POSITIONS = true;
    }

    public static class Builder extends AbstractFieldMapper.Builder<Builder, RoutingFieldMapper> {

        public Builder() {
            super(Defaults.NAME);
            store = Defaults.STORE;
            index = Defaults.INDEX;
        }

        @Override public RoutingFieldMapper build(BuilderContext context) {
            return new RoutingFieldMapper(store, index);
        }
    }

    protected RoutingFieldMapper() {
        this(Defaults.STORE, Defaults.INDEX);
    }

    protected RoutingFieldMapper(Field.Store store, Field.Index index) {
        super(new Names(Defaults.NAME, Defaults.NAME, Defaults.NAME, Defaults.NAME), index, store, Defaults.TERM_VECTOR, 1.0f, Defaults.OMIT_NORMS, Defaults.OMIT_TERM_FREQ_AND_POSITIONS,
                Lucene.KEYWORD_ANALYZER, Lucene.KEYWORD_ANALYZER);
    }

    @Override public String value(Document document) {
        Fieldable field = document.getFieldable(names.indexName());
        return field == null ? null : value(field);
    }

    @Override public String value(Fieldable field) {
        return field.stringValue();
    }

    @Override public String valueFromString(String value) {
        return value;
    }

    @Override public String valueAsString(Fieldable field) {
        return value(field);
    }

    @Override public String indexedValue(String value) {
        return value;
    }

    @Override protected Field parseCreateField(ParseContext context) throws IOException {
        if (context.externalValueSet()) {
            String routing = (String) context.externalValue();
            if (routing != null) {
                if (!indexed() && !stored()) {
                    context.ignoredValue(names.indexName(), routing);
                    return null;
                }
                return new Field(names.indexName(), routing, store, index);
            }
        }
        return null;

    }

    @Override protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override public void toXContent(XContentBuilder builder, Params params) throws IOException {
        // if all are defaults, no sense to write it at all
        if (index == Defaults.INDEX && store == Defaults.STORE) {
            return;
        }
        builder.startObject(CONTENT_TYPE);
        if (index != Defaults.INDEX) {
            builder.field("index", index.name().toLowerCase());
        }
        if (store != Defaults.STORE) {
            builder.field("store", store.name().toLowerCase());
        }
        builder.endObject();
    }

    @Override public void merge(XContentMapper mergeWith, MergeContext mergeContext) throws MergeMappingException {
        // do nothing here, no merging, but also no exception
    }
}