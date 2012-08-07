package com.levelup.logcatutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LogCollectorEmail extends LogCollectorFile {

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

		if (mLogBuilder!=null) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			emailIntent.setType("message/rfc822"/*"text/plain"*/);
			emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(mLogBuilder));
			if (mRecipients!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mRecipients);
			if (mTitle!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTitle);
			if (mText!=null)
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mText);

			Intent intent = Intent.createChooser(emailIntent, mDialogTitle!=null ? mDialogTitle : mTitle);
			if (mContext instanceof Activity)
				((Activity)mContext).startActivityForResult(intent, 0x105);
			else
				mContext.startActivity(intent);
		}
	}

}
