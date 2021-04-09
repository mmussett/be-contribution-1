/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.custom.channel;

import com.tibco.cep.runtime.model.event.SimpleEvent;

public class TestExtendedEventImpl extends ExtendedDefaultEventImpl {

	TestExtendedEventImpl(SimpleEvent simpleEvent) throws Exception {
		super(simpleEvent);
	}
	
	public static TestExtendedEventImpl createInstance(SimpleEvent simpleEvent) throws Exception {
		return new TestExtendedEventImpl(simpleEvent);
	}

}
