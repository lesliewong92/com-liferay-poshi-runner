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

package com.liferay.poshi.runner.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiLogEntry {

	public PoshiLogEntry(
		Element element, String event, String status,
		Map<String, Object> variables) {

		_element = element;
		_event = event;
		_status = status;
		_variables = variables;
	}

	public void addToChildPoshiLogEntries(PoshiLogEntry poshiLogEntry) {
		_childPoshiLogEntries.add(poshiLogEntry);
	}

	public Element getElement() {
		return _element;
	}

	public String getEvent() {
		return _event;
	}

	public PoshiLogEntry getLastChildLoggerElement() {
		if (!_childPoshiLogEntries.isEmpty()) {
			return _childPoshiLogEntries.get(_childPoshiLogEntries.size() - 1);
		}

		return null;
	}

	public String getStatus() {
		return _status;
	}

	public void setEvent(String event) {
		_event = event;
	}

	public void setExecutionException(Exception e) {
		_executionException = e;
	}

	public void setStatus(String status) {
		_status = status;
	}

	@Override
	public String toString() {
		return toString(0);
	}

	public String toString(int tabstop) {
		StringBuilder sb = new StringBuilder();

		sb.append(_element.getName());
		sb.append(" ");
		sb.append("Status:" + _status);

		if (_event != null) {
			sb.append(" (" + _event + ")");
		}

		for (Attribute attribute : (List<Attribute>)_element.attributes()) {
			sb.append(" ");
			sb.append(attribute.getName());
			sb.append("=");
			sb.append(attribute.getValue());
		}

		for (PoshiLogEntry childLoggerElement : _childPoshiLogEntries) {
			sb.append("\n");

			for (int i = 0; i < tabstop; i++) {
				sb.append("\t");
			}

			sb.append(childLoggerElement.toString(tabstop + 1));
		}

		return sb.toString();
	}

	private List<PoshiLogEntry> _childPoshiLogEntries = new ArrayList<>();
	private final Element _element;
	private String _event;
	private Exception _executionException;
	private String _status;
	private final Map<String, Object> _variables;

}