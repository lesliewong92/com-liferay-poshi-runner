/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiStackFrame {

	public PoshiStackFrame(Element element) {
		_element = element;
	}

	public boolean containsVariable(String key) {
		return _variables.containsKey(key);
	}

	public Object getVariable(String key) {
		return _variables.get(key);
	}

	public void setException(Exception e) {
		_exception = e;
	}

	public void setVariable(String key, Object value) {
		_variables.put(key, value);
	}

	private final Element _element;
	private Exception _exception;
	private Map<String, Object> _variables = new HashMap<>();

}