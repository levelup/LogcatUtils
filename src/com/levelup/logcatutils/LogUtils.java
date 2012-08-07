package com.levelup.logcatutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogUtils {

	public interface LogHandler {
		void addNewLogLine(String line);
		void finishedReadingLogs();
	}

	public static void generateLog(final LogHandler handler) {
		if (handler!=null)
			new Thread() {
			@Override
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec("logcat -d");
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

					String line;
					while ((line = bufferedReader.readLine()) != null)
						handler.addNewLogLine(line);
					
					handler.finishedReadingLogs();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
