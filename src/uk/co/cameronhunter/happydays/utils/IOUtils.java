package uk.co.cameronhunter.happydays.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class IOUtils {

	public static void closeQuietly( Closeable closeable ) {
		try {
			if ( closeable != null ) closeable.close();
		}
		catch ( IOException ignore ) {}
	}

	public static String fileToString( InputStream in ) throws IOException {
		byte[] buffer = new byte[in.available()];
		while ( in.read( buffer ) != -1 );
		return new String( buffer, Charset.forName( "UTF-8" ) );
	}

	private IOUtils() {}
}
