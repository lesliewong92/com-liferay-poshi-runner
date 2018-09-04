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
import com.liferay.poshi.runner.PoshiRunnerStackTraceUtil;
import com.liferay.poshi.runner.elements.PoshiElement;
import com.liferay.poshi.runner.exception.PoshiRunnerLoggerException;
import com.liferay.poshi.runner.util.PropsValues;
import com.liferay.poshi.runner.util.StringUtil;
import com.liferay.poshi.runner.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * @author Michael Hashimoto
 */
public final class ScriptLoggerHandler {

	public static void generateScriptLog(String namespacedClassCommandName)
		throws PoshiRunnerLoggerException {

		try {
			_scriptLogLoggerElement = new LoggerElement("xmlLogContainer");

			_scriptLogLoggerElement.setClassName("xml-log-container");
			_scriptLogLoggerElement.setName("ul");

			LoggerElement headerLoggerElement = new LoggerElement();

			headerLoggerElement.setClassName("header");
			headerLoggerElement.setName("li");

			LoggerElement lineContainerLoggerElement = new LoggerElement();

			lineContainerLoggerElement.setClassName("line-container");
			lineContainerLoggerElement.setID(null);
			lineContainerLoggerElement.setName("div");

			LoggerElement lineLoggerElement = new LoggerElement();

			lineLoggerElement.setClassName("test-case-command");
			lineLoggerElement.setID(null);
			lineLoggerElement.setName("h3");
			lineLoggerElement.setText(namespacedClassCommandName);

			lineContainerLoggerElement.addChildLoggerElement(lineLoggerElement);

			headerLoggerElement.addChildLoggerElement(
				lineContainerLoggerElement);

			LoggerElement childContainerLoggerElement = new LoggerElement();

			childContainerLoggerElement.setClassName("child-container");
			childContainerLoggerElement.setID(null);
			childContainerLoggerElement.setName("ul");

			String className =
				PoshiRunnerGetterUtil.
					getClassNameFromNamespacedClassCommandName(
						namespacedClassCommandName);
			String namespace =
				PoshiRunnerGetterUtil.
					getNamespaceFromNamespacedClassCommandName(
						namespacedClassCommandName);

			PoshiElement setUpElement =
				(PoshiElement)PoshiRunnerContext.getTestCaseCommandElement(
					className + "#set-up", namespace);

			if (setUpElement != null) {
				PoshiRunnerStackTraceUtil.startStackTrace(
					namespace + "." + className + "#set-up", "test-case");

				childContainerLoggerElement.addChildLoggerElement(
					_getLoggerElementFromElement(setUpElement));

				PoshiRunnerStackTraceUtil.emptyStackTrace();
			}

			PoshiRunnerStackTraceUtil.startStackTrace(
				namespacedClassCommandName, "test-case");

			String classCommandName =
				PoshiRunnerGetterUtil.
					getClassCommandNameFromNamespacedClassCommandName(
						namespacedClassCommandName);

			PoshiElement testCaseElement =
				(PoshiElement)PoshiRunnerContext.getTestCaseCommandElement(
					classCommandName, namespace);

			childContainerLoggerElement.addChildLoggerElement(
				_getLoggerElementFromElement(testCaseElement));

			PoshiRunnerStackTraceUtil.emptyStackTrace();

			PoshiElement tearDownElement =
				(PoshiElement)PoshiRunnerContext.getTestCaseCommandElement(
					className + "#tear-down", namespace);

			if (tearDownElement != null) {
				PoshiRunnerStackTraceUtil.startStackTrace(
					namespace + "." + className + "#tear-down", "test-case");

				childContainerLoggerElement.addChildLoggerElement(
					_getLoggerElementFromElement(tearDownElement));

				PoshiRunnerStackTraceUtil.emptyStackTrace();
			}

			headerLoggerElement.addChildLoggerElement(
				childContainerLoggerElement);

			_scriptLogLoggerElement.addChildLoggerElement(headerLoggerElement);
		}
		catch (Throwable t) {
			throw new PoshiRunnerLoggerException(t.getMessage(), t);
		}
	}

	public static LoggerElement getScriptLoggerElement(String stackTrace) {
		return _loggerElements.get(stackTrace);
	}

	public static String getScriptLogText() {
		return _scriptLogLoggerElement.toString();
	}

	public static void updateStatus(Element element, String status) {
		PoshiRunnerStackTraceUtil.setCurrentElement(element);

		String stackTrace = PoshiRunnerStackTraceUtil.getSimpleStackTrace();

		if (stackTrace.contains(".function")) {
			return;
		}

		LoggerElement loggerElement = getScriptLoggerElement(stackTrace);

		loggerElement.setAttribute("data-status01", status);
	}

	private static LoggerElement _getBtnContainerLoggerElement(
		PoshiElement element) {

		LoggerElement btnContainerLoggerElement = new LoggerElement();

		btnContainerLoggerElement.setClassName("btn-container");
		btnContainerLoggerElement.setName("div");

		StringBuilder sb = new StringBuilder();

		sb.append(
			_getLineNumberItemText(
				PoshiRunnerGetterUtil.getLineNumber(element)));

		List<PoshiElement> childElements = element.poshiElements();

		if ((!childElements.isEmpty() && !_isExecutingFunction(element) &&
			 !_isExecutingGroovyScript(element) &&
			 !_isExecutingMethod(element)) ||
			_isExecutingMacro(element) || _isExecutingTestCase(element)) {

			sb.append(_getBtnItemText("btn-collapse"));
		}

		btnContainerLoggerElement.setText(sb.toString());

		return btnContainerLoggerElement;
	}

	private static String _getBtnItemText(String className) {
		LoggerElement loggerElement = new LoggerElement();

		if (className.equals("btn-collapse")) {
			loggerElement.setAttribute(
				"data-btnlinkid", "collapse-" + _btnLinkCollapseId);
		}
		else if (className.equals("btn-var")) {
			loggerElement.setAttribute(
				"data-btnlinkid", "var-" + _btnLinkVarId);
		}

		loggerElement.setClassName("btn " + className);
		loggerElement.setID(null);
		loggerElement.setName("button");

		return loggerElement.toString();
	}

	private static LoggerElement _getChildContainerLoggerElement(
			PoshiElement element)
		throws Exception {

		return _getChildContainerLoggerElement(element, null);
	}

	private static LoggerElement _getChildContainerLoggerElement(
			PoshiElement element, PoshiElement rootElement)
		throws Exception {

		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setAttribute(
			"data-btnlinkid", "collapse-" + _btnLinkCollapseId);

		loggerElement.setClassName("child-container collapse collapsible");
		loggerElement.setName("ul");

		if (rootElement != null) {
			List<PoshiElement> rootVarElements = rootElement.poshiElements(
				"var");

			for (PoshiElement rootVarElement : rootVarElements) {
				loggerElement.addChildLoggerElement(
					_getVarLoggerElement(rootVarElement));
			}
		}

		if (element != null) {
			List<PoshiElement> childElements = element.poshiElements();

			for (PoshiElement childElement : childElements) {
				String childElementName = childElement.getName();

				if (childElementName.equals("description") ||
					childElementName.equals("echo")) {

					loggerElement.addChildLoggerElement(
						_getEchoLoggerElement(childElement));
				}
				else if (childElementName.equals("execute")) {
					if (childElement.attributeValue("function") != null) {
						loggerElement.addChildLoggerElement(
							_getFunctionExecuteLoggerElement(childElement));
					}
					else if (childElement.attributeValue("groovy-script") !=
								 null) {

						loggerElement.addChildLoggerElement(
							_getGroovyScriptLoggerElement(childElement));
					}
					else if (childElement.attributeValue("macro") != null) {
						loggerElement.addChildLoggerElement(
							_getMacroExecuteLoggerElement(
								childElement, "macro"));
					}
					else if (Validator.isNotNull(
								 childElement.attributeValue(
									 "macro-desktop")) &&
							 !PropsValues.MOBILE_BROWSER) {

						loggerElement.addChildLoggerElement(
							_getMacroExecuteLoggerElement(
								childElement, "macro-desktop"));
					}
					else if (Validator.isNotNull(
								 childElement.attributeValue("macro-mobile")) &&
							 PropsValues.MOBILE_BROWSER) {

						loggerElement.addChildLoggerElement(
							_getMacroExecuteLoggerElement(
								childElement, "macro-mobile"));
					}
					else if (childElement.attributeValue("method") != null) {
						loggerElement.addChildLoggerElement(
							_getMethodExecuteLoggerElement(childElement));
					}
					else if (childElement.attributeValue("test-case") != null) {
						loggerElement.addChildLoggerElement(
							_getTestCaseExecuteLoggerElement(childElement));
					}
				}
				else if (childElementName.equals("fail")) {
					loggerElement.addChildLoggerElement(
						_getFailLoggerElement(childElement));
				}
				else if (childElementName.equals("for") ||
						 childElementName.equals("task")) {

					loggerElement.addChildLoggerElement(
						_getForLoggerElement(childElement));
				}
				else if (childElementName.equals("if")) {
					loggerElement.addChildLoggerElement(
						_getIfLoggerElement(childElement));
				}
				else if (childElementName.equals("return")) {
					loggerElement.addChildLoggerElement(
						_getReturnLoggerElement(childElement));
				}
				else if (childElementName.equals("var")) {
					loggerElement.addChildLoggerElement(
						_getVarLoggerElement(childElement));
				}
				else if (childElementName.equals("while")) {
					loggerElement.addChildLoggerElement(
						_getWhileLoggerElement(childElement));
				}
			}
		}

		return loggerElement;
	}

	private static LoggerElement _getClosingLineContainerLoggerElement() {
		LoggerElement closingLineContainerLoggerElement = new LoggerElement();

		closingLineContainerLoggerElement.setClassName("line-container");
		closingLineContainerLoggerElement.setName("div");

		closingLineContainerLoggerElement.setText("}");

		return closingLineContainerLoggerElement;
	}

	private static LoggerElement _getEchoLoggerElement(PoshiElement element) {
		return _getLineGroupLoggerElement("echo", element);
	}

	private static LoggerElement _getFailLoggerElement(PoshiElement element) {
		return _getLineGroupLoggerElement(element);
	}

	private static LoggerElement _getForLoggerElement(PoshiElement element)
		throws Exception {

		return _getLoggerElementFromElement(element);
	}

	private static LoggerElement _getFunctionExecuteLoggerElement(
		PoshiElement element) {

		return _getLineGroupLoggerElement("function", element);
	}

	private static LoggerElement _getGroovyScriptLoggerElement(
		PoshiElement element) {

		return _getLineGroupLoggerElement("groovy-script", element);
	}

	private static LoggerElement _getIfLoggerElement(PoshiElement element)
		throws Exception {

		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setName("div");

		LoggerElement ifLoggerElement = _getLineGroupLoggerElement(
			"conditional", element);

		ifLoggerElement.addChildLoggerElement(
			_getChildContainerLoggerElement(element.poshiElement("then")));
		ifLoggerElement.addChildLoggerElement(
			_getClosingLineContainerLoggerElement());

		loggerElement.addChildLoggerElement(ifLoggerElement);

		List<PoshiElement> elseIfElements = element.poshiElements("elseif");

		for (PoshiElement elseIfElement : elseIfElements) {
			loggerElement.addChildLoggerElement(
				_getIfLoggerElement(elseIfElement));
		}

		PoshiElement elseElement = element.poshiElement("else");

		if (elseElement != null) {
			loggerElement.addChildLoggerElement(
				_getLoggerElementFromElement(elseElement));
		}

		return loggerElement;
	}

	private static LoggerElement _getLineContainerLoggerElement(
		PoshiElement element) {

		LoggerElement lineContainerLoggerElement = new LoggerElement();

		lineContainerLoggerElement.setClassName("line-container");
		lineContainerLoggerElement.setName("div");

		if (element.attributeValue("macro") != null) {
			lineContainerLoggerElement.setAttribute(
				"onmouseout", "macroHover(this, false)");
			lineContainerLoggerElement.setAttribute(
				"onmouseover", "macroHover(this, true)");
		}

		String logStatement = StringUtil.trim(element.getLogStatement());

		String name = element.getName();

		List<PoshiElement> elements = element.poshiElements();

		if (!name.equals("execute") && !elements.isEmpty()) {
			logStatement += " {";
		}

		lineContainerLoggerElement.setText(
			_getLineItemText("name", logStatement));

		return lineContainerLoggerElement;
	}

	private static LoggerElement _getLineGroupLoggerElement(
		PoshiElement element) {

		return _getLineGroupLoggerElement(null, element);
	}

	private static LoggerElement _getLineGroupLoggerElement(
		String className, PoshiElement element) {

		_btnLinkCollapseId++;
		_btnLinkVarId++;

		PoshiRunnerStackTraceUtil.setCurrentElement(element);

		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setClassName("line-group");
		loggerElement.setName("li");

		if (Validator.isNotNull(className)) {
			loggerElement.addClassName(className);
		}

		loggerElement.addChildLoggerElement(
			_getBtnContainerLoggerElement(element));
		loggerElement.addChildLoggerElement(
			_getLineContainerLoggerElement(element));

		_loggerElements.put(
			PoshiRunnerStackTraceUtil.getSimpleStackTrace(), loggerElement);

		return loggerElement;
	}

	private static String _getLineItemText(String className, String text) {
		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setClassName(className);
		loggerElement.setID(null);
		loggerElement.setName("span");
		loggerElement.setText(text);

		return loggerElement.toString();
	}

	private static LoggerElement _getLineNumberItem(int lineNumber) {
		LoggerElement loggerElement = new LoggerElement();

		loggerElement.setClassName("line-number");
		loggerElement.setID(null);
		loggerElement.setName("div");
		loggerElement.setText(String.valueOf(lineNumber));

		return loggerElement;
	}

	private static String _getLineNumberItemText(int lineNumber) {
		LoggerElement loggerElement = _getLineNumberItem(lineNumber);

		return loggerElement.toString();
	}

	private static LoggerElement _getLoggerElementFromElement(
			PoshiElement element)
		throws Exception {

		LoggerElement loggerElement = _getLineGroupLoggerElement(element);

		loggerElement.addChildLoggerElement(
			_getChildContainerLoggerElement(element));
		loggerElement.addChildLoggerElement(
			_getClosingLineContainerLoggerElement());

		return loggerElement;
	}

	private static LoggerElement _getMacroCommandLoggerElement(
			String namespacedClassCommandName)
		throws Exception {

		String classCommandName =
			PoshiRunnerGetterUtil.
				getClassCommandNameFromNamespacedClassCommandName(
					namespacedClassCommandName);
		String namespace = PoshiRunnerStackTraceUtil.getCurrentNamespace(
			namespacedClassCommandName);

		PoshiElement commandElement =
			(PoshiElement)PoshiRunnerContext.getMacroCommandElement(
				classCommandName, namespace);

		String className =
			PoshiRunnerGetterUtil.getClassNameFromNamespacedClassCommandName(
				namespacedClassCommandName);

		PoshiElement rootElement =
			(PoshiElement)PoshiRunnerContext.getMacroRootElement(
				className, namespace);

		return _getChildContainerLoggerElement(commandElement, rootElement);
	}

	private static LoggerElement _getMacroExecuteLoggerElement(
			PoshiElement executeElement, String macroType)
		throws Exception {

		LoggerElement loggerElement = _getLineGroupLoggerElement(
			"macro", executeElement);

		String classCommandName = executeElement.attributeValue(macroType);

		PoshiRunnerStackTraceUtil.pushStackTrace(executeElement);

		loggerElement.addChildLoggerElement(
			_getMacroCommandLoggerElement(classCommandName));

		PoshiRunnerStackTraceUtil.popStackTrace();

		return loggerElement;
	}

	private static LoggerElement _getMethodExecuteLoggerElement(
			PoshiElement executeElement)
		throws Exception {

		return _getLineGroupLoggerElement("method", executeElement);
	}

	private static LoggerElement _getReturnLoggerElement(PoshiElement element) {
		return _getLineGroupLoggerElement("return", element);
	}

	private static LoggerElement _getTestCaseCommandLoggerElement(
			String namespacedClassCommandName)
		throws Exception {

		PoshiElement commandElement =
			(PoshiElement)PoshiRunnerContext.getTestCaseCommandElement(
				namespacedClassCommandName,
				PoshiRunnerGetterUtil.
					getNamespaceFromNamespacedClassCommandName(
						namespacedClassCommandName));

		String className =
			PoshiRunnerGetterUtil.getClassNameFromNamespacedClassCommandName(
				namespacedClassCommandName);

		PoshiElement rootElement =
			(PoshiElement)PoshiRunnerContext.getTestCaseRootElement(
				className,
				PoshiRunnerGetterUtil.
					getNamespaceFromNamespacedClassCommandName(
						namespacedClassCommandName));

		return _getChildContainerLoggerElement(commandElement, rootElement);
	}

	private static LoggerElement _getTestCaseExecuteLoggerElement(
			PoshiElement executeElement)
		throws Exception {

		LoggerElement loggerElement = _getLineGroupLoggerElement(
			"test-case", executeElement);

		String namespacedClassCommandName = executeElement.attributeValue(
			"test-case");

		PoshiRunnerStackTraceUtil.pushStackTrace(executeElement);

		loggerElement.addChildLoggerElement(
			_getTestCaseCommandLoggerElement(namespacedClassCommandName));

		PoshiRunnerStackTraceUtil.popStackTrace();

		return loggerElement;
	}

	private static LoggerElement _getVarLoggerElement(PoshiElement element) {
		return _getLineGroupLoggerElement("var", element);
	}

	private static LoggerElement _getWhileLoggerElement(PoshiElement element)
		throws Exception {

		LoggerElement loggerElement = _getLineGroupLoggerElement(element);

		loggerElement.addChildLoggerElement(
			_getChildContainerLoggerElement(element.poshiElement("then")));

		loggerElement.addChildLoggerElement(
			_getClosingLineContainerLoggerElement());

		return loggerElement;
	}

	private static boolean _isExecutingFunction(PoshiElement element) {
		if (element.attributeValue("function") != null) {
			return true;
		}

		return false;
	}

	private static boolean _isExecutingGroovyScript(PoshiElement element) {
		if (element.attributeValue("groovy-script") != null) {
			return true;
		}

		return false;
	}

	private static boolean _isExecutingMacro(PoshiElement element) {
		if ((element.attributeValue("macro") != null) ||
			(element.attributeValue("macro-desktop") != null) ||
			(element.attributeValue("macro-mobile") != null)) {

			return true;
		}

		return false;
	}

	private static boolean _isExecutingMethod(PoshiElement element) {
		if (element.attributeValue("method") != null) {
			return true;
		}

		return false;
	}

	private static boolean _isExecutingTestCase(PoshiElement element) {
		if (element.attributeValue("test-case") != null) {
			return true;
		}

		return false;
	}

	private static int _btnLinkCollapseId;
	private static int _btnLinkVarId;
	private static final Map<String, LoggerElement> _loggerElements =
		new HashMap<>();
	private static LoggerElement _scriptLogLoggerElement;

}