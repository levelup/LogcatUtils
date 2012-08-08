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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LogCollectorEmail extends LogCollectorFile {

	public static final int RESULT_SHARE = 0x3155;
	
	private final String[] mRecipients;
	private final String mTitle;
	private final String mDialogTitle;
	private final String mText;

	public LogCollectorEmail(Context context, String [] recipients, String emailTitle, String dialogTitle, String text) {
		super(context);
		mRecipients = recipients;
		mTitle = emailTitle;
		mDialogTitle = dialogTitle;
		mText = text;
	}
	
	@Override
	public void finishedReadingLogs() {
		super.finishedReadingLogs();

		if (mFileIsCreated) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			emailIntent.setType("message/rfc822"/*"text/plain"*/);
			emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(getLogFile()));
			if (mRecipients!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mRecipients);
			if (mTitle!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
			if (mText!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mText);

			Intent intent = Intent.createChooser(emailIntent, mDialogTitle!=null ? mDialogTitle : mTitle);
			if (mContext instanceof Activity)
				((Activity)mContext).startActivityForResult(intent, RESULT_SHARE);
			else
				mContext.startActivity(intent);
		}
	}

}
