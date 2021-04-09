/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.metric.store.elasticsearch;

import org.elasticsearch.action.DocWriteRequest;

/**
 * Actual record object that gets published. Every entity(Concept/Event) is converted to an 
 * ElasticSearchRecord before publishing it to Elasticsearch Server.
 */
public class ElasticSearchRecord {

	private DocWriteRequest<?> request;
	
	public ElasticSearchRecord(DocWriteRequest<?> request) {
		this.request = request;
	}
	
	public DocWriteRequest<?> getRequest() {
		return request;
	}
}
