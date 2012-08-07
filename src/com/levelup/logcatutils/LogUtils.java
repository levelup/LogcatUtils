package com.levelup.logcatutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogUtils {

	public interface LogHandler {
		void addNewLogLine(String line);
		void finishedReadingLogs();
	}

	public static void generateLog(final LogHandler handler, final String[] extraStrings) {
		if (handler!=null)
			new Thread() {
			@Override
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec("logcat -d -v time");
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

					final String pidTest = String.format("(% 5d)", android.os.Process.myPid());
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						boolean add = false;
						if (line.contains(pidTest))
							add = true;
						else if (extraStrings!=null) {
							for (int i=0;i<extraStrings.length; ++i)
								if (line.contains(extraStrings[i])) {
									add = true;
									break;
								}
						}
						if (add)
							handler.addNewLogLine(line);
					}

					handler.finishedReadingLogs();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
