package org.fxp.android.apk.ad;

import java.util.jar.JarEntry;

import org.fxp.android.apk.ApkBean;

public class Umengclick extends AdPattern {

	public Umengclick() {
		super("UmengClick");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isExist(ApkBean apk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExistEntry(JarEntry jarEntry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExistManifest(ApkBean apk) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExistClass(ApkBean apk) {
		String dstString="com.mobclick.android.MobclickAgent".replace('.', '/');
		for(String className:apk.classNames){
			if(className.equals(dstString))
				return true;
		}
		return false;
	}

}
