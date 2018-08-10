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

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiExecutorStack {

	public PoshiExecutorStack() {
		rootPoshiStackTraceElement = null;
	}

	/**
	 * Wraps the stack needs of an instance executor
	 * Will simulate the "push" and "pops" when executing on new depth
	 * Always keep track of root element (the start) as well as current element
	 * When initialized, create root PoshiStackTraceElement from root element
	 * Root Element should be element of current context
	 *
	 */
	public PoshiExecutorStack(Element element) {
		rootPoshiStackTraceElement = new PoshiStackTraceElement(element, null);
	}

	public void addElement(Element element) {
		PoshiStackTraceElement newStackTraceElement =
			new PoshiStackTraceElement(element, _currentPoshiStackTraceElement);

		PoshiStackTraceElement parentStackTraceElement =
			_currentPoshiStackTraceElement.getParentElement();

		parentStackTraceElement.addChildStackTraceElement(newStackTraceElement);

		_currentPoshiStackTraceElement = newStackTraceElement;
	}

	public void addValue(String key, Object value) {
		_currentPoshiStackTraceElement.addVariable(key, value);
	}

	public void empty() {
		_currentPoshiStackTraceElement = null;
		rootPoshiStackTraceElement = null;
	}

	public Object getValue(String key) {
		return _currentPoshiStackTraceElement.getVariable(key);
	}

	public PoshiStackTraceElement peek() {
		return _currentPoshiStackTraceElement;
	}

	public PoshiStackTraceElement pop() {
		PoshiStackTraceElement currentElement = _currentPoshiStackTraceElement;

		_currentPoshiStackTraceElement = currentElement.getParentElement();

		return currentElement;
	}

	public void push(Element element) {
		PoshiStackTraceElement newStackTraceElement =
			new PoshiStackTraceElement(element, _currentPoshiStackTraceElement);

		// LOGIC IS WEIRD HERE??? NOT EXPLICIT IN SHOWING IT'S THE FIRST ELEMENT

		_currentPoshiStackTraceElement.addChildStackTraceElement(
			newStackTraceElement);

		_currentPoshiStackTraceElement = newStackTraceElement;
	}

	protected PoshiStackTraceElement rootPoshiStackTraceElement;

	private PoshiStackTraceElement _currentPoshiStackTraceElement;

}