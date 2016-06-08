package archivo.digital.androidstart.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public interface JSONAware<T> {

    T mapFromJSONObject(JSONObject object) throws JSONException;

    JSONObject toJSONObject() throws JSONException;

}
