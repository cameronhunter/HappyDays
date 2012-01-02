package uk.co.cameronhunter.happydays;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference implements TimePicker.OnTimeChangedListener {

	private Pair<Integer, Integer> value;

	public TimePreference( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}
	
	public TimePreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}

	@Override
	protected View onCreateDialogView() {
		
		TimePicker timePicker = new TimePicker( getContext() );
		timePicker.setIs24HourView( true );
		timePicker.setOnTimeChangedListener( this );

		value = getPersistedValue();

		timePicker.setCurrentHour( value.first );
		timePicker.setCurrentMinute( value.second );

		return timePicker;
	}
	
	@Override
	protected void onDialogClosed( boolean positiveResult ) {
		if ( positiveResult ) {
			if ( isPersistent() ) {
				persistString( valueToString( value ) );
			}
			callChangeListener( positiveResult );
		}
	}

	@Override
	public void onTimeChanged( TimePicker view, int hourOfDay, int minute ) {
		this.value = Pair.create( hourOfDay, minute );
	}

	public String getValue() {
		return valueToString( getPersistedValue() );
	}

	private Pair<Integer, Integer> stringToValue( String value ) {
		if ( value == null ) return null;
		String[] parts = value.split( getContext().getString( R.string.time_join_character ) );
		return Pair.create( Integer.valueOf( parts[0] ), Integer.valueOf( parts[1] ) );
	}

	private Pair<Integer, Integer> getPersistedValue() {
		return stringToValue( getPersistedString( getContext().getString( R.string.default_time ) ) );
	}

	private String valueToString( Pair<Integer, Integer> value ) {
		Context context = getContext();
		return context.getString( R.string.time_format, value.first, context.getString( R.string.time_join_character ), value.second );
	}

	@Override
	public boolean isPersistent() {
		return true;
	}
}
