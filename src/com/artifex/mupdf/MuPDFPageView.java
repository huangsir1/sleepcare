package com.artifex.mupdf;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Point;
/**
 * 用于显示pdf视图，包含了pdf文件的信息
 */
public class MuPDFPageView extends PageView {
	
	private final MuPDFCore mCore;
	public static int pdfSizeX;
	public static int pdfSizeY;
	/**
	 * 文件当前在屏幕显示的坐标原点。
	 */
	public static int pdfPatchX;
	public static int pdfPatchY;
	/**
	 * pdf文件原始宽和高
	 */
	public static int pdfPatchWidth;
	public static int pdfPatchHeight;
	
	public MuPDFPageView(Context c, MuPDFCore core, Point parentSize) {
		super(c, parentSize);
		mCore = core;
	}

//	public int hitLinkPage(float x, float y) {
//		// Since link highlighting was implemented, the super class
//		// PageView has had sufficient information to be able to
//		// perform this method directly. Making that change would
//		// make MuPDFCore.hitLinkPage superfluous.
//		float scale = mSourceScale*(float)getWidth()/(float)mSize.x;
//		float docRelX = (x - getLeft())/scale;
//		float docRelY = (y - getTop())/scale;
//		//Log.e("info", "Page--->"+);
//		return mCore.hitLinkPage(mPageNumber, docRelX, docRelY);
//	}
	
	/**
	 * patchX,patchY:pdf文件当前在屏幕显示的坐标原点。
	 * patchWidth，patchHeight：pdf文件原始宽和高。
	 * 在这里获取pdf原始宽高和当前屏幕显示坐标位置，用于定位书写坐标。
	 */
	@Override
	protected void drawPage(Bitmap bm, int sizeX, int sizeY,
			int patchX, int patchY, int patchWidth, int patchHeight) {
		mCore.drawPage(mPageNumber, bm, sizeX, sizeY, patchX, patchY, patchWidth, patchHeight);
		pdfSizeX = sizeX;
		pdfSizeY = sizeY;
		pdfPatchX = patchX;
		pdfPatchY = patchY;
		pdfPatchWidth = patchWidth;
		pdfPatchHeight = patchHeight;
	}

	@Override
	protected LinkInfo[] getLinkInfo() {
		return mCore.getPageLinks(mPageNumber);
	}
	
}
