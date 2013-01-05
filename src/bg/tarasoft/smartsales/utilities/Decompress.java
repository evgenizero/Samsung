package bg.tarasoft.smartsales.utilities;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
	private String zipDir;
	private String extractedDir;

	public Decompress(String zipDir, String extractedDir) {
		this.zipDir = zipDir;
		this.extractedDir = extractedDir;

		dirChecker("");
	}

	public void unzip() {
		try {
			FileInputStream fin = new FileInputStream(zipDir);
			ZipInputStream zipStream = new ZipInputStream(fin);
			ZipEntry ze = null;
			while ((ze = zipStream.getNextEntry()) != null) {
				Log.v("Decompress", "Unzipping " +extractedDir+ ze.getName());

				if (ze.isDirectory()) {
					dirChecker(ze.getName());
				} else {

					int size;
					byte[] buffer = new byte[2048];

					FileOutputStream outStream = new FileOutputStream(extractedDir
							+ ze.getName());
					BufferedOutputStream bufferOut = new BufferedOutputStream(
							outStream, buffer.length);

					while ((size = zipStream.read(buffer, 0, buffer.length)) != -1) {
						bufferOut.write(buffer, 0, size);
					}

					bufferOut.flush();
					bufferOut.close();
				}

			}
			zipStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Decompress", "unzip", e);
		}

	}

	private void dirChecker(String dir) {
		File f = new File(extractedDir + dir);

		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}
}