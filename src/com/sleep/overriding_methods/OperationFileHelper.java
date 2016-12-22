package com.sleep.overriding_methods;

import java.io.File;

/**
 * 操作文件帮助类
 */
public class OperationFileHelper {
	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
			} else {
				for (File f : childFile) {
					RecursionDeleteFile(f);
				}
				file.delete();
			}
		}
	}

	// 递归删除文件夹下的文件
	public static void DeleteDataFile(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				f.delete();
			}
		}
	}
}
