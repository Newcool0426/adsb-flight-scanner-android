package com.flightaware.android.flightfeeder.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.flightaware.android.flightfeeder.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class UsbDvbDetector {

	private static final String TAG = "FlightFeeder";
	private static HashSet<String> sDevices = new HashSet<String>();

	public static UsbDevice isValidDeviceConnected(Context context) {
		if (sDevices.size() == 0) {
			XmlResourceParser xrp = null;
			try {
				xrp = context.getResources().getXml(R.xml.device_filter);
				xrp.next();
				int eventType = xrp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG
							&& xrp.getName().equalsIgnoreCase("usb-device")) {
						String ident = xrp.getAttributeIntValue(0, -1) + "-"
								+ xrp.getAttributeIntValue(1, -1);

						if (!ident.contains("-1"))
							sDevices.add(ident);
					}

					eventType = xrp.next();
				}
			} catch (Exception ex) {
				android.util.Log.e(TAG, "Error parsing device_filter.xml", ex);
			} finally {
				if (xrp != null)
					xrp.close();
			}

			android.util.Log.d(TAG, "Device filter has " + sDevices.size() + " entries");
			for (String s : sDevices) {
				android.util.Log.d(TAG, "  filter: " + s);
			}
		}

		UsbManager usbManager = (UsbManager) context
				.getSystemService(Context.USB_SERVICE);

		HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		while (deviceIterator.hasNext()) {
			UsbDevice device = deviceIterator.next();
			String ident = device.getVendorId() + "-" + device.getProductId();

			android.util.Log.d(TAG, "Checking USB device: " + ident);

			if (sDevices.contains(ident)) {
				android.util.Log.d(TAG, "MATCH found: " + ident);
				return device;
			}
		}

		android.util.Log.d(TAG, "No matching USB device found");
		return null;
	}

	public static int getFilterSize() {
		return sDevices.size();
	}

	public static java.util.HashSet<String> getFilterSet() {
		return sDevices;
	}

	// prevent construction
	private UsbDvbDetector() {

	}
}
