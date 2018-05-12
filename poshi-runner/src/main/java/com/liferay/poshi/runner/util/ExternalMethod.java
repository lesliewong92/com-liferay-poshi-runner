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

package com.liferay.poshi.runner.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openqa.selenium.StaleElementReferenceException;

/**
 * @author Kevin Yen
 */
public class ExternalMethod {

	public static Object execute(
			Method method, Object object, Object[] parameters)
		throws Exception {

		Object returnObject = null;

		parameters = _transformParameters(method, parameters);

		try {
			returnObject = method.invoke(object, parameters);
		}
		catch (Exception e1) {
			Throwable throwable = e1.getCause();

			if (throwable instanceof StaleElementReferenceException) {
				StringBuilder sb = new StringBuilder();

				sb.append("\nElement turned stale while running ");
				sb.append(method.getName());
				sb.append(". Retrying in ");
				sb.append(PropsValues.TEST_RETRY_COMMAND_WAIT_TIME);
				sb.append("seconds.");

				System.out.println(sb.toString());

				try {
					returnObject = method.invoke(object, parameters);
				}
				catch (Exception e2) {
					throwable = e2.getCause();

					throw new Exception(throwable.getMessage(), e2);
				}
			}
			else {
				throw new Exception(throwable.getMessage(), e1);
			}
		}

		if (returnObject == null) {
			return "";
		}

		return returnObject;
	}

	public static Object execute(
			String methodName, Object object, Object[] parameters)
		throws Exception {

		Class<?> clazz = object.getClass();

		Method method = getMethod(clazz, methodName, parameters);

		return execute(method, object, parameters);
	}

	public static Object execute(String className, String methodName)
		throws Exception {

		Object[] parameters = {};

		return execute(className, methodName, parameters);
	}

	public static Object execute(
			String className, String methodName, Object[] parameters)
		throws Exception {

		Class<?> clazz = Class.forName(className);

		Method method = getMethod(clazz, methodName, parameters);

		int modifiers = method.getModifiers();

		Object object = null;

		if (!Modifier.isStatic(modifiers)) {
			object = clazz.newInstance();
		}

		return execute(method, object, parameters);
	}

	public static Method getMethod(
			Class clazz, String methodName, Object[] parameters)
		throws Exception {

		for (Method method : clazz.getMethods()) {
			if (!methodName.equals(method.getName())) {
				continue;
			}

			Class<?>[] parameterTypes = method.getParameterTypes();

			boolean varArgs = method.isVarArgs();

			if (!varArgs && (parameterTypes.length != parameters.length)) {
				continue;
			}

			boolean parameterTypesMatch = true;

			for (int i = 0; i < parameterTypes.length; i++) {
				Object parameter = parameters[i];

				if ((i == (parameterTypes.length - 1)) && varArgs) {
					Class<?> varArgParameterType =
						parameterTypes[i].getComponentType();

					boolean varArgMatch = true;

					for (int k = i; k < parameters.length; k++) {
						if (Objects.equals(parameter, _POSHI_NULL_NOTATION)) {
							continue;
						}

						if (varArgParameterType != parameters[k].getClass()) {
							varArgMatch = false;

							break;
						}
					}

					if (!varArgMatch) {
						parameterTypesMatch = false;
					}

					break;
				}

				if (Objects.equals(parameter, _POSHI_NULL_NOTATION)) {
					continue;
				}

				boolean childClass = false;

				Class<?> currentParameterClass = parameter.getClass();

				while (currentParameterClass != Object.class) {
					if (parameterTypes[i] == currentParameterClass) {
						childClass = true;

						break;
					}

					currentParameterClass =
						currentParameterClass.getSuperclass();
				}

				if (!childClass) {
					parameterTypesMatch = false;

					break;
				}
			}

			if (parameterTypesMatch) {
				return method;
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("Unable to find method '");
		sb.append(methodName);
		sb.append("' of class '");
		sb.append(clazz.getCanonicalName());

		if ((parameters != null) && (parameters.length != 0)) {
			sb.append("' with parameters types: (");

			for (Object parameter : parameters) {
				Class<?> parameterType = parameter.getClass();

				sb.append(parameterType.toString());

				sb.append(", ");
			}

			sb.delete(sb.length() - 2, sb.length());

			sb.append(")");
		}

		throw new IllegalArgumentException(sb.toString());
	}

	private static Object[] _transformParameters(
		Method method, Object[] parameters) {

		List<Object> transformedParameters = new ArrayList<>();

		Class<?>[] parameterTypes = method.getParameterTypes();

		for (int i = 0; i < parameterTypes.length; i++) {
			if (i == (parameterTypes.length - 1) && method.isVarArgs()) {
				Class<?> varArgParameterType =
					parameterTypes[i].getComponentType();

				Object varArgArray = Array.newInstance(
					varArgParameterType, parameters.length - i);

				int varArgArrayIndex = -1;

				for (int k = i; k < parameters.length; k++) {
					varArgArrayIndex++;

					if (Objects.equals(parameters[k], _POSHI_NULL_NOTATION)) {
						Array.set(varArgArray, varArgArrayIndex, null);

						continue;
					}

					Array.set(varArgArray, varArgArrayIndex, parameters[k]);
				}

				transformedParameters.add(varArgArray);

				break;
			}

			Object parameter = parameters[i];

			if (Objects.equals(parameter, _POSHI_NULL_NOTATION)) {
				transformedParameters.add(null);

				continue;
			}

			transformedParameters.add(parameter);
		}

		return transformedParameters.toArray();
	}

	private static final String _POSHI_NULL_NOTATION = "Poshi.NULL";

}