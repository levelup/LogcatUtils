/*
 * Copyright 2012 LevelUp Studio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.levelup.logcatutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogUtils {

	public interface LogHandler {
		void addNewLogLine(String line);
		void finishedReadingLogs();
	}

	/**
	 * 
	 * @param handler the log data receiver
	 * @param extraStrings some additional strings you want to filter in (not based on PID)
	 */
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
