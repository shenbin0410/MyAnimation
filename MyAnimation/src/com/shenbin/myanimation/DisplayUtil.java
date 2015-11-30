package com.shenbin.myanimation;

import android.content.Context;

/**
 * 这是一个dp和dx单位转换的类。
 * 用来屏幕适配的。
 * @author Administrator
 *
 */
public class DisplayUtil {

	/*** px 转换为 dip */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int dipValue = (int) (pxValue / scale + 0.5f);
		return dipValue;
	}

	/*** dip 转换为 px */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int pxValue = (int) (dipValue * scale + 0.5f);
		return pxValue;
	}

	/*** px 转换为 sp */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		int spValue = (int) (pxValue / scale + 0.5f);
		return spValue;
	}

	/*** sp 转换为 px */
	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		int pxValue = (int) (spValue * scale + 0.5f);
		return pxValue;
	}
}
