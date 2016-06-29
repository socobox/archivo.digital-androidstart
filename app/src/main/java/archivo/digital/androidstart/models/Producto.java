package archivo.digital.androidstart.models;


import archivo.digital.android.ADJSONAware;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class Producto implements ADJSONAware<Producto> {

    private String key;
    private String nombre;
    private String description;
    private String grupo;
    private GrupoProducto grupoReference;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public GrupoProducto getGrupoReference() {
        return grupoReference;
    }

    public void setGrupoReference(GrupoProducto grupoReference) {
        this.grupoReference = grupoReference;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public Producto mapFromJSONObject(JSONObject object) throws JSONException {
        setKey(object.getString("_KEY"));
        setNombre(object.getString("NOMBRE"));
        setDescription(object.getString("DESCRIPCION"));


        if (object.optJSONObject("GRUPO") == null) {
            setGrupo(object.getString("GRUPO"));
        } else {
            GrupoProducto g = new GrupoProducto();
            g.mapFromJSONObject(object.getJSONObject("GRUPO"));
            setGrupo(g.getKey());
            setGrupoReference(g);
        }


        return this;
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("_KEY", getKey());
        obj.put("NOMBRE", getNombre());
        obj.put("DESCRIPCION", getDescription());
        obj.put("GRUPO", getGrupo());
        return obj;
    }
}
