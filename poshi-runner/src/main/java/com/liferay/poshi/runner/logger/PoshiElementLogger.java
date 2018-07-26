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

import com.liferay.poshi.runner.PoshiRunnerExecutor;
import com.liferay.poshi.runner.PoshiRunnerVariablesUtil;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * @author Leslie Wong
 */
public class PoshiElementLogger {

	public static void fail(Element element, Exception e) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, null, "fail",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		poshiLogEntry.setExecutionException(e);

		_addPoshiLoggerElement(poshiLogEntry);
	}

	public static PoshiLogEntry getLastPoshiLogEntry() {
		if (!_poshiLogEntries.isEmpty()) {
			return _poshiLogEntries.get(_poshiLogEntries.size() - 1);
		}

		return null;
	}

	public static String getString() {
		StringBuilder sb = new StringBuilder();

		for (PoshiLogEntry poshiElementLogger : _poshiLogEntries) {
			sb.append(poshiElementLogger.toString(1));
			sb.append("\n");
		}

		return sb.toString();
	}

	public static void pass(Element element) {
		pass(element, null);
	}

	public static void pass(Element element, String event) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, event, "pass",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		_addPoshiLoggerElement(poshiLogEntry);
	}

	public static void pend(Element element) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, null, "pending",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		_addPoshiLoggerElement(poshiLogEntry);
	}

	public static void startLog() {
		_poshiLogEntries = new ArrayList<>();
	}

	public static void updateExecutingPoshiLoggerElementEvent(String event) {
		if (PoshiRunnerExecutor.isExecutionStackEmpty()) {
			throw new RuntimeException(
				"Failed to update execution stack with event, the execution " +
					"stack is empty");
		}

		PoshiLogEntry executingPoshiLoggerElement =
			PoshiRunnerExecutor.getExecutingElement();

		PoshiLogEntry lastChildLoggerElement =
			executingPoshiLoggerElement.getLastChildLoggerElement();

		lastChildLoggerElement.setEvent(event);
	}

	public static void warn(Element element, Exception e) {
		PoshiLogEntry poshiLogEntry = new PoshiLogEntry(
			element, e.getMessage(), "warn",
			PoshiRunnerVariablesUtil.getCommandMapVariables());

		poshiLogEntry.setExecutionException(e);

		_addPoshiLoggerElement(poshiLogEntry);
	}

	private static void _addPoshiLoggerElement(PoshiLogEntry poshiLogEntry) {
		PoshiLogEntry lastPoshiLogEntry = getLastPoshiLogEntry();

		String status = lastPoshiLogEntry.getStatus();

		if (status.equals("pending")) {
			PoshiLogEntry executingPoshiLoggerElement =
				PoshiRunnerExecutor.getExecutingElement();

			executingPoshiLoggerElement.addToChildPoshiLogEntries(
				poshiLogEntry);
		}
		else {
			_poshiLogEntries.add(poshiLogEntry);
		}
	}

	private static List<PoshiLogEntry> _poshiLogEntries;

}