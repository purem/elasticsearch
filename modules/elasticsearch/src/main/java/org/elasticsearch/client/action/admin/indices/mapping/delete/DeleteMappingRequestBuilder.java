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

package org.elasticsearch.client.action.admin.indices.mapping.delete;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.action.admin.indices.support.BaseIndicesRequestBuilder;
import org.elasticsearch.common.unit.TimeValue;

/**
 * @author kimchy (shay.banon)
 */
public class DeleteMappingRequestBuilder extends BaseIndicesRequestBuilder<DeleteMappingRequest, DeleteMappingResponse> {

    public DeleteMappingRequestBuilder(IndicesAdminClient indicesClient) {
        super(indicesClient, new DeleteMappingRequest());
    }

    public DeleteMappingRequestBuilder setIndices(String... indices) {
        request.indices(indices);
        return this;
    }

    /**
     * The type of the mapping to remove.
     */
    public DeleteMappingRequestBuilder setType(String type) {
        request.type(type);
        return this;
    }

    /**
     * Sets the master node timeout in case the master has not yet been discovered.
     */
    public DeleteMappingRequestBuilder setMasterNodeTimeout(TimeValue timeout) {
        request.masterNodeTimeout(timeout);
        return this;
    }

    @Override protected void doExecute(ActionListener<DeleteMappingResponse> listener) {
        client.deleteMapping(request, listener);
    }
}