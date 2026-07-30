package com.flightaware.android.flightfeeder.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class UsbDvbDetector {

	private static HashSet<String> sDevices = new HashSet<String>();

	static {
		int[][] ids = {
			{ 0x0bda, 0x2832 }, { 0x0bda, 0x2838 },
			{ 0x0413, 0x6680 }, { 0x0413, 0x6f0f },
			{ 0x0458, 0x707f }, { 0x0ccd, 0x00a9 },
			{ 0x0ccd, 0x00b3 }, { 0x0ccd, 0x00b4 },
			{ 0x0ccd, 0x00b5 }, { 0x0ccd, 0x00b7 },
			{ 0x0ccd, 0x00b8 }, { 0x0ccd, 0x00b9 },
			{ 0x0ccd, 0x00c0 }, { 0x0ccd, 0x00c6 },
			{ 0x0ccd, 0x00d3 }, { 0x0ccd, 0x00d7 },
			{ 0x0ccd, 0x00e0 }, { 0x1554, 0x5020 },
			{ 0x15f4, 0x0131 }, { 0x185b, 0x0620 },
			{ 0x185b, 0x0650 }, { 0x185b, 0x0680 },
			{ 0x1b80, 0xd393 }, { 0x1b80, 0xd394 },
			{ 0x1b80, 0xd395 }, { 0x1b80, 0xd397 },
			{ 0x1b80, 0xd398 }, { 0x1b80, 0xd39d },
			{ 0x1b80, 0xd3a4 }, { 0x1b80, 0xd3a8 },
			{ 0x1b80, 0xd3af }, { 0x1b80, 0xd3b0 },
			{ 0x1d19, 0x1101 }, { 0x1d19, 0x1102 },
			{ 0x1d19, 0x1103 }, { 0x1d19, 0x1104 },
			{ 0x1f4d, 0xa803 }, { 0x1f4d, 0xb803 },
			{ 0x1f4d, 0xc803 }, { 0x1f4d, 0xd286 },
			{ 0x1f4d, 0xd803 },
		};
		for (int[] pair : ids) {
			sDevices.add(pair[0] + "-" + pair[1]);
		}
	}

	public static UsbDevice isValidDeviceConnected(Context context) {
		UsbManager usbManager = (UsbManager) context
				.getSystemService(Context.USB_SERVICE);

		HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		while (deviceIterator.hasNext()) {
			UsbDevice device = deviceIterator.next();
			String ident = device.getVendorId() + "-" + device.getProductId();

			if (sDevices.contains(ident))
				return device;
		}

		return null;
	}

	public static int getFilterSize() {
		return sDevices.size();
	}

	public static java.util.HashSet<String> getFilterSet() {
		return sDevices;
	}

	private UsbDvbDetector() {

	}
}
