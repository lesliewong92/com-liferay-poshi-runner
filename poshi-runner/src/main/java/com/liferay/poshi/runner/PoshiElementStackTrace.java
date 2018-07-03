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

import java.util.Stack;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiElementStackTrace {

	public PoshiElementStackTrace(Element commandElement) {
		_commandElement = commandElement;
	}

	public void pop() {
		_stackTrace.pop();
	}

	public void push(Element element) {
		_stackTrace.push(element);
	}

	@Override
	public String toString() {
		return null;
	}

	private static final Stack<Element> _stackTrace = new Stack<>();

	private final Element _commandElement;

}