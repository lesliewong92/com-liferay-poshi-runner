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
public class PoshiExecutionHistoryNode {

	public PoshiExecutionHistoryNode(Element element) {
		this(element, null);
	}

	public PoshiExecutionHistoryNode(
		Element element, PoshiExecutionHistoryNode parentNode) {

		_element = element;
		_parentNode = parentNode;
	}

	public void addVariable(String key, Object value) {
		_variables.put(key, value);
	}

	public List<PoshiExecutionHistoryNode> getChildNodes() {
		return _childNodes;
	}

	public PoshiExecutionHistoryNode getLastChildNode() {
		return _lastChildNode;
	}

	public PoshiExecutionHistoryNode getRootNode() {
		PoshiExecutionHistoryNode rootNode = this;

		while (!rootNode.isRootNode()) {
			rootNode = _parentNode;
		}

		return rootNode;
	}

	public List<PoshiExecutionHistoryNode> getStackTrace() {
		List<PoshiExecutionHistoryNode> stacktrace = new ArrayList<>();

		PoshiExecutionHistoryNode rootNode = this;

		while (!rootNode.isRootNode()) {
			stacktrace.add(rootNode);

			rootNode = _parentNode;
		}

		return stacktrace;
	}

	public Object getVariable(String key) {
		return _variables.get(key);
	}

	public void insert(Element element) {
		PoshiExecutionHistoryNode childNode = new PoshiExecutionHistoryNode(
			element, this);

		_childNodes.add(childNode);

		_lastChildNode = childNode;
	}

	public boolean isRootNode() {
		if (_parentNode == null) {
			return true;
		}

		return false;
	}

	public boolean isVariableSet(String key) {
		return _variables.containsKey(key);
	}

	protected PoshiExecutionHistoryNode getParentNode() {
		return _parentNode;
	}

	private List<PoshiExecutionHistoryNode> _childNodes = new ArrayList<>();
	private final Element _element;
	private PoshiExecutionHistoryNode _lastChildNode;
	private PoshiExecutionHistoryNode _parentNode;
	private Map<String, Object> _variables = new HashMap<>();

}