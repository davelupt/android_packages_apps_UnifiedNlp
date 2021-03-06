package org.microg.nlp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import org.microg.nlp.Preferences;
import org.microg.nlp.R;
import org.microg.nlp.geocode.GeocodeService;

import static org.microg.nlp.api.NlpApiConstants.ACTION_GEOCODER_BACKEND;

public class GeocoderBackendPreference extends AbstractBackendPreference {
    public GeocoderBackendPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogTitle(R.string.configure_geocoder_backends);
        setTitle(R.string.configure_geocoder_backends);
    }

    @Override
    protected void onValueChanged() {
        GeocodeService.reloadLocationService(getContext());
    }

    @Override
    protected Intent buildBackendIntent() {
        return new Intent(ACTION_GEOCODER_BACKEND);
    }

    @Override
    protected String defaultValue() {
        return new Preferences(getContext()).getDefaultGeocoderBackends();
    }
}
