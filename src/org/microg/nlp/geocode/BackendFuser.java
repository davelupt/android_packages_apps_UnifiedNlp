package org.microg.nlp.geocode;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import org.microg.nlp.Preferences;

import java.util.ArrayList;
import java.util.List;

import static org.microg.nlp.api.NlpApiConstants.ACTION_GEOCODER_BACKEND;

class BackendFuser {
    private final List<BackendHelper> backendHelpers = new ArrayList<BackendHelper>();
    private final Context context;

    public BackendFuser(Context context) {
        this.context = context;
        reset();
    }
    
    public void bind() {
        for (BackendHelper backendHelper : backendHelpers) {
            backendHelper.bind();
        }
    }
    
    public void unbind() {
        for (BackendHelper backendHelper : backendHelpers) {
            backendHelper.unbind();
        }
    }

    public List<Address> getFromLocation(double latitude, double longitude, int maxResults,
            String locale) {
        if (backendHelpers.isEmpty())
            return null;
        ArrayList<Address> result = new ArrayList<Address>();
        for (BackendHelper backendHelper : backendHelpers) {
            List<Address> backendResult = backendHelper
                    .getFromLocation(latitude, longitude, maxResults, locale);
            if (backendResult != null) {
                result.addAll(backendResult);
            }
        }
        return result;
    }

    public List<Address> getFromLocationName(String locationName, int maxResults,
            double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude,
            double upperRightLongitude, String locale) {
        if (backendHelpers.isEmpty())
            return null;
        ArrayList<Address> result = new ArrayList<Address>();
        for (BackendHelper backendHelper : backendHelpers) {
            List<Address> backendResult = backendHelper
                    .getFromLocationName(locationName, maxResults, lowerLeftLatitude,
                            lowerLeftLongitude, upperRightLatitude, upperRightLongitude, locale);
            if (backendResult != null) {
                result.addAll(backendResult);
            }
        }
        return result;
    }

    public void reset() {
        unbind();
        backendHelpers.clear();
        for (String backend : Preferences
                .splitBackendString(new Preferences(context).getGeocoderBackends())) {
            String[] parts = backend.split("/");
            if (parts.length == 2) {
                Intent intent = new Intent(ACTION_GEOCODER_BACKEND);
                intent.setPackage(parts[0]);
                intent.setClassName(parts[0], parts[1]);
                backendHelpers.add(new BackendHelper(context, intent));
            }
        }
    }
}
