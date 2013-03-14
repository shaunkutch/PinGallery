package com.plastku.pingallery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileUtils {
	private static final String tag = "FileUtils";
	private static final String tempPath = "pingallery";
	private static final String tempVideoPath = tempPath + File.separator + "video";
	private static final String tempVideoValidPath = tempVideoPath + File.separator + "valid";
	private static final String tempVideoInValidPath = tempVideoPath + File.separator + "invalid";

	public static void clearTempFiles(Context context){
		// sort files
		File cacheDir = getFolder(context, tempPath);
		List<File> filesList = Arrays.asList(cacheDir.listFiles());
		
		int filesNumber = filesList.size();
		for (int i = 0; i < filesNumber; i++) {
			filesList.get(i).delete();
		}
	}
	/**
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static String readRawResource(Context context, int resourceId, String fileName) {
		return readRawResource(context, resourceId, "micr", fileName);
	}

	/**
	 * @param context
	 * @param resourceId
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public static String readRawResource(Context context, int resourceId, String folderName, String fileName) {
		String result = "";
		InputStream is = null;
		FileOutputStream os = null;
		try {
			is = context.getResources().openRawResource(resourceId);
			File localAssetDir = context.getDir(folderName, Context.MODE_PRIVATE);
			File localFile = new File(localAssetDir, fileName);
			os = new FileOutputStream(localFile);
			byte[] buffer = new byte[4096];

			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			result = localFile.getAbsolutePath();
		}
		catch (Exception e) {
			Log.e(tag, "Error readRawResource: " + e.getMessage());
		}
		finally {
			closeInputStream(is);
			closeOutputStream(os);
		}
		return result;
	}

	/**
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static String storeBitmapInTempFolder(Context context, Bitmap bitmap) {
		String filePath = getTempImageFileName(context);
		return storeBitmap(bitmap, filePath);
	}

	public static String storeVideoFrame(Context context, Bitmap bitmap, String fileName, boolean isValid) {
		
		if (!fileName.endsWith(".jpeg") && !fileName.endsWith(".jpg")) {
			fileName += ".jpg";
		}
		
		String filePath = (isValid ? getRootDirForTempValidVideo(context) : getRootDirForTempInValidVideo(context)) + File.separator + fileName;
		return storeBitmap(bitmap, filePath);
	}

	/**
	 * @param context
	 * @param bitmap
	 * @param name
	 * @return
	 */
	public static String storeBitmapInTempFolder(Context context, Bitmap bitmap, String name) {
		if (!name.endsWith(".jpeg") && !name.endsWith(".jpg")) {
			name += ".jpg";
		}
		String filePath = getTempFilePath(context) + name;
		return storeBitmap(bitmap, filePath);
	}

	/**
	 * @param context
	 * @param bitmap
	 * @param name
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String filePath) {
		Log.d(tag, "getBitmap:" + filePath);
		Bitmap bitmap = decodeFile(new File(filePath), -1, -1);
		return bitmap;
	}

	/**
	 * @param f
	 * @param resolution
	 * @return
	 */
	public static Bitmap decodeFile(File f, int maxWidthResolution, int maxHeightResolution) {
		FileInputStream fileInputStream = null;
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			fileInputStream = new FileInputStream(f);
			BitmapFactory.decodeStream(fileInputStream, null, o);
			fileInputStream = null;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;

			if (maxWidthResolution > -1 && maxHeightResolution > -1) {
				while (true) {
					if (width_tmp / 2 < maxWidthResolution || height_tmp / 2 < maxHeightResolution)
						break;

					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPreferredConfig = Config.RGB_565;

			fileInputStream = new FileInputStream(f);
			return BitmapFactory.decodeStream(fileInputStream, null, o2);
		}
		catch (Exception e) {
			Log.e(tag, "FileUtils.decodeFile", e);
		}
		finally {
			close(fileInputStream);
		}
		return null;
	}

	private static void close(InputStream fis) {
		if (fis != null) {
			try {
				fis.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void close(OutputStream ous) {
		if (ous != null) {
			try {
				ous.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param context
	 * @return
	 */
	private static String getTempImageFileName(Context context) {
		return getTempFilePath(context) + "temp.jpg";
	}

	/**
	 * @param context
	 * @return
	 */
	public static File getRootDirForTempImage(Context context) {

		return getFolder(context, tempPath);
	}

	/**
	 * @param context
	 * @return
	 */
	public static File getRootDirForTempValidVideo(Context context) {

		return getFolder(context, tempVideoValidPath);
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static File getRootDirForTempInValidVideo(Context context) {

		return getFolder(context, tempVideoInValidPath);
	}
	
	/**
	 * @param context
	 * @param path
	 * @return
	 */
	private static File getFolder(Context context, String path) {
		File cacheDir;

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), path);
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}
		else {
			cacheDir = new File(context.getFilesDir(), path);
		}

		return cacheDir;
	}

	/**
	 * @param context
	 * @return
	 */
	private static String getTempFilePath(Context context) {
		return getRootDirForTempImage(context) + File.separator;
	}

	public static String getTempImagePath(Context context, String imageName) {
		if (!imageName.endsWith(".jpeg") || !imageName.endsWith(".jpg")) {
			imageName += ".jpg";
		}
		return getTempFilePath(context) + imageName;
	}

	/**
	 * /**
	 * 
	 * @param bitmap
	 * @param filePath
	 * @return
	 */
	public static String storeBitmap(Bitmap bitmap, String filePath) {
		FileOutputStream out = null;
		try {
			File file = new File(filePath);
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			return file.getAbsolutePath();
		}
		catch (Exception e) {
			Log.e(tag, "Error storing a bitmap: " + e.getMessage());
		}
		finally {
			closeOutputStream(out);
		}
		return null;
	}

	/**
	 * @param context
	 * @param data
	 * @param fileName
	 * @return
	 */
	public static String storeByteArrayImageInTempFolder(Context context, byte[] data, String fileName) {
		String filePath = getTempFilePath(context) + fileName + ".jpg";

		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(data);
			fos.close();
		}
		catch (FileNotFoundException e) {
			Log.e(tag, "File not found: " + e.getMessage());
		}
		catch (IOException e) {
			Log.e(tag, "Error accessing file: " + e.getMessage());
		}
		return filePath;
	}

	/**
	 * @param context
	 * @param data
	 * @param fileName
	 * @return
	 */
	public static byte[] getByteArrayImage(Context context, String filePath) {

		byte[] data = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			data = new byte[fis.available()];
			while (fis.read(data) > -1) {
			}
			fis.close();
		}
		catch (FileNotFoundException e) {
			Log.e(tag, "File not found: " + e.getMessage());
		}
		catch (IOException e) {
			Log.e(tag, "Error accessing file: " + e.getMessage());
		}
		finally {
			closeInputStream(fis);
		}
		return data;
	}

	/**
	 * @param imagePath
	 */
	public static boolean deleteFile(String imagePath) {
		if (StringUtils.isEmptyOrNull(imagePath)) {
			return false;
		}

		File file = new File(imagePath);
		return file.delete();
	}

	/**
	 * @param is
	 */
	private static void closeInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			}
			catch (IOException e) {
				Log.e(tag, "Error closing a stream: " + e.getMessage());
			}
		}
		is = null;
	}

	/**
	 * @param os
	 */
	private static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			}
			catch (IOException e) {
				Log.e(tag, "Error closing a stream: " + e.getMessage());
			}
		}
		os = null;
	}

}