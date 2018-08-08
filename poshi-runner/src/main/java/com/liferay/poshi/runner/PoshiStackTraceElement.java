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
public class PoshiStackTraceElement {

	public PoshiStackTraceElement(Element element, String filePath) {
		_element = element;
		_filePath = filePath;
	}

	public void addVariable(String name, Object value) {
		_variables.put(name, value);
	}

	public Object getVariable(String name) {
		return _variables.get(name);
	}

	public boolean issetVariable(String name) {
		return _variables.containsKey(name);
	}

	private final Element _element;
	private final String _filePath;
	private Map<String, Object> _variables = new HashMap<>();

}