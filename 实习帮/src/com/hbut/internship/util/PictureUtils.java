package com.hbut.internship.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

/**
 * 图片缩放工具
 */
public class PictureUtils {

	public static Bitmap getScaledBitmap(String path, int destWidth,
			int destHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;

		int inSampleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) {
			if (srcWidth > srcHeight) {
				inSampleSize = Math.round(srcHeight / destHeight);
			} else {
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}

		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		return BitmapFactory.decodeFile(path, options);
	}

	@SuppressLint("NewApi")
	public static Bitmap getScaledBitmap(String path, Activity activity) {
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);

		return getScaledBitmap(path, size.x, size.y);
	}

	public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, os);
		if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			os.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
		float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		is = new ByteArrayInputStream(os.toByteArray());
		bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		// 压缩好比例大小后再进行质量压缩
		// return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
		return bitmap;
	}
	
	/**
	 * 本地int转Bitmap 
	 * @param photoID
	 * @return
	 */
	public static Bitmap IntToBitmap(int photoID){
		Resources r = MyApplicationUtil.getContext().getResources();
		InputStream is = r.openRawResource(photoID);
		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		Bitmap bmp = bmpDraw.getBitmap();
		return bmp;
	}
	
	public static Bitmap decodeSampleBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// 第一次解析将inJustDecodeBounds设置为true,来获取图片大小。
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(res, resId, options);
		// 调用下面定义的方法计算inSampleSize值
		options.inSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
		//使用获取到的inSampleSize值两次解析图片。
		options.inJustDecodeBounds=false;
		return BitmapFactory.decodeResource(res, resId, options);

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		//源图片的高度和宽度
		final int height=options.outHeight;
		final int width=options.outWidth;
		int inSampleSize=1;
		if (height>reqHeight||width>reqWidth) {
			//计算出实际宽高和目标宽高的比率
			final int heightRatio=Math.round((float)height/(float)reqHeight);
			final int widthRatio=Math.round((float)width/(float)reqWidth);
			//选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			//一定都会大于等于目标的宽和高
			inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
		}
		return inSampleSize;
	}
}
