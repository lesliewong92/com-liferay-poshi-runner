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

import com.liferay.poshi.runner.selenium.SeleniumUtil;
import com.liferay.poshi.runner.util.ExternalMethod;
import com.liferay.poshi.runner.util.GetterUtil;
import com.liferay.poshi.runner.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Karen Dang
 * @author Michael Hashimoto
 */
public class PoshiRunnerVariablesUtil {

	// public static void clear() {
	// 	_commandMap.clear();
	// 	_commandMapStack.clear();
	// 	_executeMap.clear();
	// 	_staticMap.clear();
	// }

	// public static boolean containsKeyInCommandMap(String key) {
	// 	return _commandMap.containsKey(replaceCommandVars(key));
	// }

	// public static boolean containsKeyInExecuteMap(String key) {
	// 	return _executeMap.containsKey(replaceCommandVars(key));
	// }

	// public static boolean containsKeyInStaticMap(String key) {
	// 	return _staticMap.containsKey(replaceCommandVars(key));
	// }

	// public static String getReplacedCommandVarsString(String token)
	// 	throws Exception {

	// 	if (token == null) {
	// 		return null;
	// 	}

	// 	Object tokenObject = replaceCommandVars(token);

	// 	return tokenObject.toString();
	// }

	// public static String getStringFromCommandMap(String key) {
	// 	if (containsKeyInCommandMap((String)replaceCommandVars(key))) {
	// 		Object object = getValueFromCommandMap(key);

	// 		return object.toString();
	// 	}

	// 	return null;
	// }

	// public static String getStringFromExecuteMap(String key) {
	// 	if (containsKeyInExecuteMap((String)replaceCommandVars(key))) {
	// 		Object object = getValueFromExecuteMap(key);

	// 		return object.toString();
	// 	}

	// 	return null;
	// }

	// public static String getStringFromStaticMap(String key) {
	// 	if (containsKeyInStaticMap((String)replaceStaticVars(key))) {
	// 		Object object = getValueFromExecuteMap(key);

	// 		return object.toString();
	// 	}

	// 	return null;
	// }

	// public static Object getValueFromCommandMap(String key) {
	// 	return _commandMap.get(replaceCommandVars(key));
	// }

	// public static Object getValueFromExecuteMap(String key) {
	// 	return _executeMap.get(replaceCommandVars(key));
	// }

	// public static Object getValueFromStaticMap(String key) {
	// 	return _staticMap.get(replaceCommandVars(key));
	// }

	// public static void popCommandMap() {
	// 	_commandMap = _commandMapStack.pop();

	// 	_commandMap.putAll(_staticMap);

	// 	_executeMap = new HashMap<>();
	// }

	// public static void pushCommandMap() {
	// 	_commandMapStack.push(_commandMap);

	// 	_commandMap = _executeMap;

	// 	_commandMap.putAll(_staticMap);

	// 	_executeMap = new HashMap<>();
	// }

	// public static void putIntoCommandMap(String key, Object value)
	// 	throws Exception {

	// 	if (value instanceof String) {
	// 		_commandMap.put(
	// 			(String)replaceCommandVars(key),
	// 			replaceCommandVars((String)value));
	// 	}
	// 	else {
	// 		_commandMap.put((String)replaceCommandVars(key), value);
	// 	}
	// }

	// public static void putIntoExecuteMap(String key, Object value)
	// 	throws Exception {

	// 	if (value instanceof String) {
	// 		_executeMap.put(
	// 			(String)replaceCommandVars(key),
	// 			replaceCommandVars((String)value));
	// 	}
	// 	else {
	// 		_executeMap.put((String)replaceCommandVars(key), value);
	// 	}
	// }

	// public static void putIntoStaticMap(String key, Object value)
	// 	throws Exception {

	// 	if (value instanceof String) {
	// 		_staticMap.put(
	// 			(String)replaceCommandVars(key),
	// 			replaceCommandVars((String)value));
	// 	}
	// 	else {
	// 		_staticMap.put((String)replaceCommandVars(key), value);
	// 	}
	// }

	/* NEW */
	public static String getValueFromMapAsString(
		String key, Map<String, Object> map) {

		if (map.containsKey(key)) {
			Object object = map.get(key);

			if (object != null) {
				return object.toString();
			}
		}

		return null;
	}

	/* NEW */
	public static Object resolveReferences(
		String token, Map<String, Object> map) {

		Matcher matcher = _pattern.matcher(token);

		if (matcher.matches() && map.containsKey(matcher.group(1))) {
			return map.get(matcher.group(1));
		}

		matcher.reset();

		while (matcher.find() && map.containsKey(matcher.group(1))) {
			String varValue = getValueFromMapAsString(matcher.group(1), map);

			token = StringUtil.replace(token, matcher.group(), varValue);
		}

		return token;
	}

	// public static Object replaceCommandVars(String token) {
	// 	Matcher matcher = _pattern.matcher(token);

	// 	if (matcher.matches() && _commandMap.containsKey(matcher.group(1))) {
	// 		return getValueFromCommandMap(matcher.group(1));
	// 	}

	// 	matcher.reset();

	// 	while (matcher.find() && _commandMap.containsKey(matcher.group(1))) {
	// 		String varValue = getStringFromCommandMap(matcher.group(1));

	// 		token = StringUtil.replace(token, matcher.group(), varValue);
	// 	}

	// 	return token;
	// }

	// public static Object replaceExecuteVars(String token) {
	// 	Matcher matcher = _pattern.matcher(token);

	// 	if (matcher.matches() && _executeMap.containsKey(matcher.group(1))) {
	// 		return getValueFromExecuteMap(matcher.group(1));
	// 	}

	// 	matcher.reset();

	// 	while (matcher.find() && _executeMap.containsKey(matcher.group(1))) {
	// 		String varValue = getStringFromExecuteMap(matcher.group(1));

	// 		token = StringUtil.replace(token, matcher.group(), varValue);
	// 	}

	// 	return token;
	// }

	// public static Object replaceStaticVars(String token) {
	// 	Matcher matcher = _pattern.matcher(token);

	// 	if (matcher.matches() && _staticMap.containsKey(matcher.group(1))) {
	// 		return getValueFromStaticMap(matcher.group(1));
	// 	}

	// 	matcher.reset();

	// 	while (matcher.find() && _staticMap.containsKey(matcher.group(1))) {
	// 		String varValue = getStringFromStaticMap(matcher.group(1));

	// 		token = StringUtil.replace(token, matcher.group(), varValue);
	// 	}

	// 	return token;
	// }

	public static Object getMethodReturnValue(
			List<String> args, String className, String methodName,
			Object object, Map<String, Object> varMap)
		throws Exception {

		if (!className.equals("selenium")) {
			if (!className.contains(".")) {
				className =
					PoshiRunnerGetterUtil.getUtilityClassName(className);
			}
			else {
				if (!PoshiRunnerGetterUtil.isValidUtilityClass(className)) {
					throw new IllegalArgumentException(
						className + " is not a valid class name");
				}
			}
		}

		Object[] parameters = new Object[args.size()];

		for (int i = 0; i < args.size(); i++) {
			String arg = args.get(i);

			Object parameter = resolveReferences(arg, varMap);

			if (className.endsWith("MathUtil") &&
				(parameter instanceof String)) {

				parameter = GetterUtil.getInteger((String)parameter);
			}
			else if (className.endsWith("StringUtil")) {
				parameter = String.valueOf(parameter);
			}

			parameters[i] = parameter;
		}

		Object returnObject = null;

		if (object != null) {
			returnObject = ExternalMethod.execute(
				methodName, object, parameters);
		}
		else {
			returnObject = ExternalMethod.execute(
				className, methodName, parameters);
		}

		return returnObject;
	}

	public static Object getVarMethodValue(
			String expression, String namespace, Map<String, Object> varMap)
		throws Exception {

		List<String> args = new ArrayList<>();

		int x = expression.indexOf("(");
		int y = expression.lastIndexOf(")");

		if ((x + 1) < y) {
			String parameterString = expression.substring(x + 1, y);

			Matcher parameterMatcher = _parameterPattern.matcher(
				parameterString);

			while (parameterMatcher.find()) {
				String parameterValue = parameterMatcher.group();

				if (parameterValue.startsWith("'") &&
					parameterValue.endsWith("'")) {

					parameterValue = parameterValue.substring(
						1, parameterValue.length() - 1);
				}
				else if (parameterValue.contains("#")) {
					parameterValue = PoshiRunnerContext.getPathLocator(
						parameterValue, namespace);
				}

				if (parameterValue.contains("\'")) {
					parameterValue = parameterValue.replaceAll("\\\\'", "'");
				}

				args.add(parameterValue);
			}
		}

		y = expression.indexOf("#");

		String className = expression.substring(0, y);
		String methodName = expression.substring(y + 1, x);

		Object object = null;

		if (className.equals("selenium")) {
			object = SeleniumUtil.getSelenium();
		}

		return getMethodReturnValue(
			args, className, methodName, object, varMap);
	}

	private static final Pattern _parameterPattern = Pattern.compile(
		"('([^'\\\\]|\\\\.)*'|[^',\\s]+)");
	private static final Pattern _pattern = Pattern.compile("\\$\\{([^}]*)\\}");

}