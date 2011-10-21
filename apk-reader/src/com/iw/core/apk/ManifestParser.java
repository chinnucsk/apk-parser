package com.iw.core.apk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iw.core.apk.ApkInfo.ApkScreenSupport;

public class ManifestParser extends DocumentParser{
	private static final Log log = LogFactory.getLog(ManifestParser.class);

	private ManifestParser() {
	}

	private static List<String> FindInDocument(Document doc, String keyName,
			String attribName) {
		List<String> ret = new ArrayList<String>();
		NodeList usesPermissions = doc.getElementsByTagName(keyName);

		if (usesPermissions != null) {
			for (int s = 0; s < usesPermissions.getLength(); s++) {
				Node permissionNode = usesPermissions.item(s);
				if (permissionNode.getNodeType() == Node.ELEMENT_NODE) {
					Node node = permissionNode.getAttributes().getNamedItem(
							attribName);
					if (node != null)
						ret.add(node.getNodeValue());
				}
			}
		}
		return ret;
	}

	public static boolean parserManifest(Document doc, ApkInfo info) {
		// Read every thing from AndroidManifest.xml
		info.setMinSdkVersion(FindStringInDocument(doc, "uses-sdk",
				"android:minSdkVersion"));
		info.setPackageName(FindStringInDocument(doc, "manifest", "package"));
		info.setVersionCode(FindStringInDocument(doc, "manifest",
				"android:versionCode"));
		info.setVersionName(FindStringInDocument(doc, "manifest",
				"android:versionName"));
		info.setLabel(FindStringInDocument(doc, "application", "android:label"));

		getPermissions(doc, info);
		getScreenSupport(doc, info);

		return false;
	}

	public static String FindStringInDocument(Document doc, String parent,
			String attr) {
		String ret = null;
		List<String> results = FindInDocument(doc, parent, attr);
		if (results.size() > 0)
			ret = results.get(0);
		return ret;
	}

	public static Boolean FindBooleanInDocument(Document doc, String parent,
			String attr) {
		Boolean ret = null;
		List<Boolean> results = FindBooleansInDocument(doc, parent, attr);
		if (results.size() > 0)
			ret = results.get(0);
		return ret;
	}

	public static Integer FindIntegerInDocument(Document doc, String parent,
			String attr) {
		Integer ret = null;
		List<Integer> results = FindIntegersInDocument(doc, parent, attr);
		if (results.size() > 0)
			ret = results.get(0);
		return ret;
	}

	public static List<String> FindStringsInDocument(Document doc,
			String parent, String attr) {
		return FindInDocument(doc, parent, attr);
	}

	public static List<Boolean> FindBooleansInDocument(Document doc,
			String parent, String attr) {
		List<String> results = FindInDocument(doc, parent, attr);
		List<Boolean> ret = new ArrayList<Boolean>();
		for (String result : results) {
			try {
				ret.add(Boolean.valueOf(result));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static List<Integer> FindIntegersInDocument(Document doc,
			String parent, String attr) {
		List<String> results = FindInDocument(doc, parent, attr);
		List<Integer> ret = new ArrayList<Integer>();
		for (String result : results) {
			try {
				ret.add(Integer.valueOf(result));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static int addPermissions(Document doc, ApkInfo info, String parent,
			String attr) {
		List<String> permissions = FindStringsInDocument(doc, parent, attr);
		for (String permission : permissions) {
			info.addPermission(parent, permission);
		}
		return permissions.size();
	}

	public static int getPermissions(Document doc, ApkInfo info) {
		int permissionCount = 0;
		permissionCount += addPermissions(doc, info, "uses-permission",
				"android:name");
		permissionCount += addPermissions(doc, info, "permission-group",
				"android:name");
		permissionCount += addPermissions(doc, info, "service",
				"android:permission");
		permissionCount += addPermissions(doc, info, "provider",
				"android:permission");
		permissionCount += addPermissions(doc, info, "activity",
				"android:permission");

		return permissionCount;
	}

	public static Boolean getScreenSupport(Document doc, String parent,
			String attr) {
		Boolean ret = null;
		List<Boolean> screenSupport = FindBooleansInDocument(doc, parent, attr);
		if (screenSupport.size() > 0)
			ret = screenSupport.get(0);

		return ret;
	}

	private static void getScreenSupport(Document doc, ApkInfo info) {
		ApkScreenSupport screenSupport = info.getScreenSupport();
		screenSupport.resizeable = getScreenSupport(doc, "supports-screens",
				"android:resizeable");
		screenSupport.smallScreens = getScreenSupport(doc, "supports-screens",
				"android:smallScreens");
		screenSupport.normalScreens = getScreenSupport(doc, "supports-screens",
				"android:normalScreens");
		screenSupport.largeScreens = getScreenSupport(doc, "supports-screens",
				"android:largeScreens");
		screenSupport.xlargeScreens = getScreenSupport(doc, "supports-screens",
				"android:xlargeScreens");
		screenSupport.anyDensity = getScreenSupport(doc, "supports-screens",
				"android:anyDensity");

		List<Integer> result = FindIntegersInDocument(doc, "supports-screens",
				"android:requiresSmallestWidthDp");
		if (result.size() > 0)
			screenSupport.requiresSmallestWidthDp = result.get(0);

		result = FindIntegersInDocument(doc, "supports-screens",
				"android:compatibleWidthLimitDp");
		if (result.size() > 0)
			screenSupport.compatibleWidthLimitDp = result.get(0);

		result = FindIntegersInDocument(doc, "supports-screens",
				"android:largestWidthLimitDp");
		if (result.size() > 0)
			screenSupport.largestWidthLimitDp = result.get(0);
	}
}
