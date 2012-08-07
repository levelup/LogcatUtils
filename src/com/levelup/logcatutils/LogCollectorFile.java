package com.levelup.logcatutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public abstract class LogCollectorFile implements LogUtils.LogHandler {
	public LogCollectorFile(Context context) {
		mContext = context;
	}
	
	private static final byte[] CRLN = new byte[]{'\r', '\n'};
	
	protected final Context mContext;
	protected File mLogBuilder;
	protected OutputStream mOutLogger;

	@Override
	public void addNewLogLine(String line) {
		if (mLogBuilder==null) {
			String tempDir = "Android/data/"+mContext.getPackageName()+"/logs";
			File dir = new File(Environment.getExternalStorageDirectory(), tempDir);
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				if (!dir.exists() || !dir.isDirectory()) {
					boolean mDirAsserted = dir.mkdirs();
					//mLogger.w("cache dir=" + dir.getAbsolutePath()+" asserted:"+DirAsserted);
					if (mDirAsserted) {
						File noMedia = new File(dir, ".nomedia");
						try {
							noMedia.createNewFile();
						} catch (IOException e) {
						}
					}
				}

				mLogBuilder = new File(dir, "log_" + System.currentTimeMillis() + ".txt");
				mOutLogger = null;
				try {
					mLogBuilder.createNewFile();
					mOutLogger = new BufferedOutputStream(new FileOutputStream(mLogBuilder));
				} catch (IOException e) {
					try {
						if (mOutLogger != null)
							mOutLogger.close();
					} catch (IOException ee) {
					} finally {
						mOutLogger = null;
						mLogBuilder.delete();
						mLogBuilder = null;
					}
				}
			}
		}

		try {
			if (mOutLogger!=null) {
				mOutLogger.write(line.getBytes());
				mOutLogger.write(CRLN);
			}
		} catch (IOException e) {
		}
	}

	@Override
	public void finishedReadingLogs() {
		try {
			if (mOutLogger!=null)
				mOutLogger.close();
		} catch (IOException e) {
			mOutLogger = null;
		}
	}

}
