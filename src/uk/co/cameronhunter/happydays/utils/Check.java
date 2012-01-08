package uk.co.cameronhunter.happydays.utils;

public final class Check {

	public static <T> T notNull( T object ) {
		if ( object == null ) {
			throw new IllegalArgumentException( "Object is null" );
		}
		return object;
	}
	
	private Check() {}
}
