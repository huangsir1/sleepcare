package com.artifex.mupdf;

import android.graphics.RectF;
/**
 * ��������һ�����ε��ĸ������ȸ�������
 */
public class LinkInfo extends RectF {
	public static int pageNumber;

	public LinkInfo(float l, float t, float r, float b, int p) {
		super(l, t, r, b);
		pageNumber = p;
	}
}
