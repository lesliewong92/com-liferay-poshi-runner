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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiStackTraceElement {

	public PoshiStackTraceElement(
		Element element, PoshiStackTraceElement parentPoshiStackTraceElement) {

		_element = element;
		parentElement = parentPoshiStackTraceElement;
	}

	public void addChildStackTraceElement(
		PoshiStackTraceElement poshiStackTraceElement) {

		_childElements.add(poshiStackTraceElement);
	}

	public void addVariable(String key, Object value) {
		_variables.put(key, value);
	}

	public Object getVariable(String key) {
		return _variables.get(key);
	}

	public boolean issetVariable(String key) {
		return _variables.containsKey(key);
	}

	protected PoshiStackTraceElement getParentElement() {
		return parentElement;
	}

	protected PoshiStackTraceElement parentElement;

	private List<PoshiStackTraceElement> _childElements = new ArrayList<>();
	private final Element _element;
	private Map<String, Object> _variables = new HashMap<>();

}