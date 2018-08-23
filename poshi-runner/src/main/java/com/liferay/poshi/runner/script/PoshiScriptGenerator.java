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

package com.liferay.poshi.runner.script;

import com.liferay.poshi.runner.PoshiRunnerContext;
import com.liferay.poshi.runner.elements.PoshiElement;
import com.liferay.poshi.runner.elements.PoshiNodeFactory;
import com.liferay.poshi.runner.util.Dom4JUtil;
import com.liferay.poshi.runner.util.FileUtil;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;

/**
 * @author Kenji Heigel
 */
public class PoshiScriptGenerator {

	public static String poshiFileDirName = "";

	public static boolean areElementsEqual(Element element1, Element element2)
		throws Exception {

		NodeComparator nodeComparator = new NodeComparator();

		int compare = 1;

		try {
			compare = nodeComparator.compare(element1, element2);
		}
		catch (Exception e) {
			return false;
		}

		if (compare == 0) {
			return true;
		}

		return false;
	}

	public static void generate(String filePath) throws Exception {
		PoshiElement poshiElement = null;

		try {
			poshiElement = (PoshiElement)PoshiNodeFactory.newPoshiNodeFromFile(
				filePath);

			String fileContent = FileUtil.read(filePath);

			Document document = Dom4JUtil.parse(fileContent);

			Element rootElement = document.getRootElement();

			Dom4JUtil.removeWhiteSpaceTextNodes(rootElement);

			String poshiScript = poshiElement.toPoshiScript();

			int index = filePath.lastIndexOf(".");

			PoshiElement newPoshiElement =
				(PoshiElement)PoshiNodeFactory.newPoshiNode(
					poshiScript, filePath.substring(index + 1));

			if (areElementsEqual(rootElement, poshiElement)) {
				if (areElementsEqual(rootElement, newPoshiElement)) {
					Files.write(Paths.get(filePath), poshiScript.getBytes());
				}
				else {
					System.out.println("Could not generate Poshi Script:");
					System.out.println(filePath);
				}
			}
			else {
				System.out.println("Could not generate Poshi Script:");
				System.out.println(filePath);
			}
		}
		catch (Exception e) {
		}
	}

	public static void generateAll() throws Exception {
		String[] poshiExtensions = {"macro", "testcase"};

		File poshiFileDir = new File(poshiFileDirName);

		for (File file :
				FileUtils.listFiles(poshiFileDir, poshiExtensions, true)) {

			String filePath = file.getAbsolutePath();

			generate(filePath);
		}
	}

	public static void main(String[] args) throws Exception {
		String[] poshiFileNames = {"**/*.function"};

		PoshiRunnerContext.readFiles(poshiFileNames, poshiFileDirName);

		generateAll();
	}

}