package com.vaya.elevenMeal;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class NfcEnabledActivity extends Activity {
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mIntentFilters;
	private String[][] mNFCTechLists;
	private NdefMessage mNdefMessage;
	
	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndefIntent = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefIntent.addDataType("*/*");
			mIntentFilters = new IntentFilter[] { ndefIntent };
		} catch (Exception e) {
			Log.e("TagDispatch", e.toString());
		}

		mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
		setNfcMessage("");
	}

	public static NdefRecord createNewTextRecord(String text, Locale locale,
			boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));

		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);

		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}

	protected void setNfcMessage(String message) {
		mNdefMessage = new NdefMessage(new NdefRecord[] { createNewTextRecord(
				message, Locale.ENGLISH, true) });
		mNfcAdapter.setNdefPushMessage(mNdefMessage, this);
		// mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		String action = intent.getAction();
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		String s = "";// = action + "\n\n" + tag.toString();

		Parcelable[] data = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

		if (data != null) {
			try {
				for (int i = 0; i < data.length; i++) {
					NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
					for (int j = 0; j < recs.length; j++) {
						if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN
								&& Arrays.equals(recs[j].getType(),
										NdefRecord.RTD_TEXT)) {

							byte[] payload = recs[j].getPayload();
							String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
									: "UTF-16";
							int langCodeLen = payload[0] & 0077;

							s = new String(payload, langCodeLen + 1,
									payload.length - langCodeLen - 1,
									textEncoding);
						}
					}
				}
			} catch (Exception e) {
				Log.e("TagDispatch", e.toString());
			}

		}
		Toast.makeText(this, "Tag nfc lu", Toast.LENGTH_SHORT).show();
		if (!s.isEmpty()) {
			/*Intent newIntent = new Intent(this, CompareActivity.class);
			newIntent.putExtra("message", s);
			startActivity(newIntent);*/
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent,
					mIntentFilters, mNFCTechLists);
			// mNfcAdapter.enableForegroundNdefPush(this, mNdefMessage);
			mNfcAdapter.setNdefPushMessage(mNdefMessage, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(this);
			// mNfcAdapter.disableForegroundNdefPush(this);
			mNfcAdapter.setNdefPushMessage(null, this);
		}
	}
}
