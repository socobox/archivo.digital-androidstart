package archivo.digital.androidstart.models;

import org.json.JSONException;
import org.json.JSONObject;

import archivo.digital.androidstart.utils.JSONAware;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class GrupoProducto implements JSONAware<GrupoProducto> {
    private String key;
    private String nombre;
    private float vlrDomicilio;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getVlrDomicilio() {
        return vlrDomicilio;
    }

    public void setVlrDomicilio(float vlrDomicilio) {
        this.vlrDomicilio = vlrDomicilio;
    }

    @Override
    public String toString() {
        return nombre + "\n" + vlrDomicilio;
    }

    @Override
    public GrupoProducto mapFromJSONObject(JSONObject object) throws JSONException {
        setKey(object.getString("_KEY"));
        setNombre(object.getString("NOMBRE"));
        setVlrDomicilio(object.getInt("VALOR_DOMICILIO"));
        return this;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        return null;
    }
}
