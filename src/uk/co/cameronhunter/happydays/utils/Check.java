package uk.co.cameronhunter.happydays.utils;

public final class Check {

	public static <T> T notNull( T thing ) {
		if ( thing == null ) throw new IllegalArgumentException( "Input is null" );
		return thing;
	}
	
	private Check() {}
}
