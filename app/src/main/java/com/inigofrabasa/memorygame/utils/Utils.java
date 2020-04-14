package com.inigofrabasa.memorygame.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.inigofrabasa.memorygame.MemoryGameApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okio.BufferedSource;
import okio.Okio;

public class Utils {

	public static String readFileFromAssets(String fileName) {
		try {
			InputStream input = MemoryGameApplication.Companion.getInstance().getAssets().open(fileName);
			BufferedSource source = Okio.buffer(Okio.source(input));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				return source.readByteString().string(StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			Log.e("readFileFromAssets", "exept:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static int px(int dp) {
		return (int) (MemoryGameApplication.Companion.getInstance().getAppContext().getResources().getDisplayMetrics().density * dp);
	}

	static Bitmap scaleDownImage(int resource, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(MemoryGameApplication.Companion.getInstance().getAppContext().getResources(), resource);

		options.inSampleSize = calculateSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(MemoryGameApplication.Companion.getInstance().getAppContext().getResources(), resource, options);
	}

	private static int calculateSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSize;
	}
}
