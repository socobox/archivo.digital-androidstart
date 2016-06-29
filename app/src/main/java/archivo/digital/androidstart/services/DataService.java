package archivo.digital.androidstart.services;

import android.content.Context;
import android.util.Log;

import archivo.digital.android.ADService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import archivo.digital.android.ADCallback;
import archivo.digital.android.ADQueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import archivo.digital.androidstart.models.GrupoProducto;
import archivo.digital.androidstart.models.Producto;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class DataService {
    public static final String APP_KEY = "abddef12-34abd-def43-fdeb8-abc9877";
    public static final int DOMAIN = 73;
    private String token;
    private String userName;
    private static DataService mInstance;
    private Context ctx;

    public DataService() {
    }

    public static DataService getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataService();
            mInstance.setContext(ctx);
        }
        return mInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Context getContext() {
        return ctx;
    }

    public void setContext(Context ctx) {
        this.ctx = ctx;
    }

    public void login(String user, String password, final ADCallback<Void> cb) {
        RequestParams p = new RequestParams();
        p.put("login", user);
        p.put("password", password);
        p.put("domain", DOMAIN);

        HttpService.get("/api/user/v1/login", p, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        final JSONObject o = response.getJSONObject("user");
                        setUserName(o.getString("name"));
                        setToken(response.getString("token"));
                        cb.ok(null);

                    } else {
                        cb.err("Credenciales Inválidas");
                    }

                } catch (JSONException e) {
                    cb.err(e.getMessage());
                }
            }


            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cb.err(errorResponse != null ? errorResponse.toString() : "" + throwable);
            }
        });
    }

    public void loadGrupoProductos(final ADCallback<List<GrupoProducto>> cb) {
        JSONObject obj = null;

        try {
            obj = new ADQueryBuilder(DOMAIN, "GRUPO_PRODUCTO", 1, 1000)
                    .compile();
            ADService.getInstance(ctx).findAllByQuery(token, obj, cb, GrupoProducto.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadProductosPorGrupo(final String keyGrupo, final ADCallback<List<Producto>> cb, final int page) {

        try {
            JSONObject obj = new ADQueryBuilder(DOMAIN, "PRODUCTO", page, 1000)
                    .addField(ADQueryBuilder.OP.EQ, ADQueryBuilder.ANDOR.AND, "GRUPO", keyGrupo)
                    .compile();

            ADService.getInstance(ctx).findAllByQuery(token, obj, cb, Producto.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadProducto(final String keyProducto, final ADCallback<Producto> cb) {
        JSONObject obj = null;
        try {
            obj = new ADQueryBuilder(DOMAIN, "PRODUCTO", 1, 1000)
                    .fetch(new String[]{"GRUPO"})
                    .compileWithKeys(new String[]{keyProducto});
            ADService.getInstance(ctx).getObject(token, obj, cb, Producto.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addProducto(String name, String desc, String key, final ADCallback<Void> cb){
        JSONObject obj = new JSONObject();
        try {
            obj.put("domain", DOMAIN);
            obj.put("row_model", "PRODUCTO");
            JSONArray array = new JSONArray();
            JSONObject p = new JSONObject();
            array.put(p);
            p.put("NOMBRE", name);
            p.put("DESCRIPCION", desc);
            p.put("GRUPO", key);
            obj.put("rows", array);

            Log.d("DATA", "PRODCUTO JSON:" + obj.toString());
            try {
                HttpService.postJSON(ctx, "/api/data/v1/row", token, obj, new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            if (response.getBoolean("success")) {
                                cb.ok(null);

                                //
                            } else {
                                Log.d("DATA", "ORDENS ERROR:" + response.toString());
                                cb.err("Problemas al enviar las órdenes");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            cb.err("Problemas al enviar las órdenes");
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        cb.err(errorResponse != null ? errorResponse.toString() : "" + throwable);
                    }


                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void updateProducto(Producto prod, final ADCallback<Void> cb) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("domain", DOMAIN);
            obj.put("row_model", "PRODUCTO");
            JSONArray array = new JSONArray();
            array.put(prod.toJSONObject());
            obj.put("rows", array);

            Log.d("DATA", "PRODCUTO JSON:" + obj.toString());
            try {
                HttpService.postJSON(ctx, "/api/data/v1/row/update", token, obj, new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            if (response.getBoolean("success")) {
                                cb.ok(null);

                                //
                            } else {
                                Log.d("DATA", "ORDENS ERROR:" + response.toString());
                                cb.err("Problemas al enviar las órdenes");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            cb.err("Problemas al enviar las órdenes");
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        cb.err(errorResponse != null ? errorResponse.toString() : "" + throwable);
                    }


                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteProducto(Producto prod, final ADCallback<Void> cb) {
        JSONObject obj = null;
        try {
            obj = new ADQueryBuilder(DOMAIN, "PRODUCTO")
                    .compileWithKeys(new String[]{prod.getKey()});

            Log.d("DATA", "PRODCUTO JSON:" + obj.toString());
            try {
                HttpService.postJSON(ctx, "/api/data/v1/row/delete", token, obj, new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            if (response.getBoolean("success")) {
                                cb.ok(null);

                                //
                            } else {
                                Log.d("DATA", "ORDENS ERROR:" + response.toString());
                                cb.err("Problemas al enviar las órdenes");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            cb.err("Problemas al enviar las órdenes");
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        cb.err(errorResponse != null ? errorResponse.toString() : "" + throwable);
                    }


                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
