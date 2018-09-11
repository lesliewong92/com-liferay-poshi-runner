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

import com.liferay.poshi.runner.PoshiRunnerContext;
import com.liferay.poshi.runner.PoshiRunnerGetterUtil;
import com.liferay.poshi.runner.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Michael Hashimoto
 */
public final class LoggerUtil {

	private static String _getHtmlFilePath() {
		StringBuilder sb = new StringBuilder();

		sb.append(_CURRENT_DIR_NAME);
		sb.append("/test-results/");
		sb.append(
			StringUtil.replace(
				PoshiRunnerContext.getTestCaseNamespacedClassCommandName(), "#",
				"_"));
		sb.append("/index.html");

		return sb.toString();
	}

	private static String _getSummaryLogFilePath() {
		StringBuilder sb = new StringBuilder();

		sb.append(_CURRENT_DIR_NAME);
		sb.append("/test-results/");
		sb.append(
			StringUtil.replace(
				PoshiRunnerContext.getTestCaseNamespacedClassCommandName(), "#",
				"_"));
		sb.append("/summary.html");

		return sb.toString();
	}

	private static String _readResource(String path) throws IOException {
		StringBuilder sb = new StringBuilder();

		ClassLoader classLoader = LoggerUtil.class.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(path);

		InputStreamReader inputStreamReader = new InputStreamReader(
			inputStream);

		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}

		bufferedReader.close();

		return sb.toString();
	}

	private static final String _CURRENT_DIR_NAME =
		PoshiRunnerGetterUtil.getCanonicalPath(".");

}