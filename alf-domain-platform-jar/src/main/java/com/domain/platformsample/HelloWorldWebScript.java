/**
 * Copyright (C) 2017 Alfresco Software Limited.
 * <p/>
 * This file is part of the Alfresco SDK project.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.domain.platformsample;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A demonstration Java controller for the Hello World Web Script.
 *
 * @author martin.bergljung@alfresco.com
 * @since 2.1.0
 */
public class HelloWorldWebScript extends DeclarativeWebScript {
	private static Log logger = LogFactory.getLog(HelloWorldWebScript.class);

	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<ResponseRst> result = new ArrayList<>();

		String regexNum = "^\\d+$";
		String regexFec = "^\\d{1,2}?/\\d{1,2}?/\\d{4}?$";
		SimpleDateFormat FORMATO_MDY = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat FORMATO_YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("http://localhost:8080/alfresco/service/api/audit/query/alfresco-access?verbose=true");

			String user = req.getParameter("user");
			String action = req.getParameter("action");
			String document = req.getParameter("document");
			String dateIni = req.getParameter("dateIni");
			String dateFin = req.getParameter("dateFin");

			if (user != null && user.length() > 0) {
				sb.append("&user=" + user);
			}
			if (dateIni != null && Pattern.matches(regexFec, dateIni)) {
				sb.append("&fromTime=" + FORMATO_MDY.parse(dateIni));
			}
			if (dateFin != null && Pattern.matches(regexFec, dateFin)) {
				sb.append("&toTime=" + FORMATO_MDY.parse(dateFin));
			}

			URL url = new URL(sb.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("admin", "admin".toCharArray());
				}
			});

			if (conn.getResponseCode() == 200) {
//				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				System.out.println("Output from Server .... \n");
//				JSONObject obj = new JSONObject(conn.getInputStream());
//				System.out.println(obj);
				StringBuffer json = new StringBuffer();
				Scanner sc = new Scanner(conn.getInputStream());
				while (sc.hasNext()) {
					json.append(sc.nextLine() + "\n");
				}
//				System.out.println(json.toString());
				sc.close();
				JSONObject obj = new JSONObject(json.toString());
				JSONArray arr = obj.getJSONArray("entries");
				for (int i = 0; i < arr.length(); i++) {
					String timeT = arr.getJSONObject(i).getString("time");
					timeT = timeT.replace("T", " ").substring(0, timeT.lastIndexOf(".") - 1);
					Date dateRs = FORMATO_YMD_HMS.parse(timeT);
					String userRs = "";
					String actionRs = "";
					String documentRs = "";
					if (!arr.getJSONObject(i).getJSONObject("values").isNull("/alfresco-access/transaction/action")) {
						if (arr.getJSONObject(i).getJSONObject("values").getString("/alfresco-access/transaction/type")
								.equals("cm:content")) {
//							if (!arr.getJSONObject(i).getJSONObject("values")
//									.isNull("/alfresco-access/transaction/user")) {
							userRs = arr.getJSONObject(i).getJSONObject("values")
									.getString("/alfresco-access/transaction/user");
//							}
							actionRs = arr.getJSONObject(i).getJSONObject("values")
									.getString("/alfresco-access/transaction/action");
							documentRs = arr.getJSONObject(i).getJSONObject("values")
									.getString("/alfresco-access/transaction/path");
							ResponseRst r = new ResponseRst(userRs, dateRs, actionRs,
									documentRs.substring(documentRs.lastIndexOf(":") + 1));
							result.add(r);
						}
					}
				}
//				String output = "";
//				while ((output = br.readLine()) != null) {
//					System.out.println(output);
//				}
			} else {
				System.err.println("Failed : HTTP error code : " + conn.getResponseCode());
			}
			if (action != null && action.length() > 0) {
				for (Iterator<ResponseRst> iterator = result.iterator(); iterator.hasNext();) {
					ResponseRst responseRst = (ResponseRst) iterator.next();
					String rAct = responseRst.getAction().toUpperCase();
					if (!rAct.contains(action.toUpperCase())) {
						iterator.remove();
					}
				}
			}
			if (document != null && document.length() > 0) {
				for (Iterator<ResponseRst> iterator = result.iterator(); iterator.hasNext();) {
					ResponseRst responseRst = (ResponseRst) iterator.next();
					String rDoc = responseRst.getDocument().toUpperCase();
					if (!rDoc.contains(document.toUpperCase())) {
						iterator.remove();
					}
				}
			}
			model.put("result", result);
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
}