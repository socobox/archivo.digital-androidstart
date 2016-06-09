package archivo.digital.androidstart.services;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import archivo.digital.androidstart.models.GrupoProducto;
import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.utils.QueryBuilder;
import archivo.digital.androidstart.utils.ResponseListener;
import cz.msebera.android.httpclient.Header;

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

    public void login(String user, String password, final ResponseListener<Void> cb) {
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

    public void loadGrupoProductos(final ResponseListener<ArrayList<GrupoProducto>> cb) {
        JSONObject obj = null;
        try {
            obj = new QueryBuilder(DOMAIN, "GRUPO_PRODUCTO", 1, 1000)
                    .compile();
            Log.d("DATA", "QUERY:" + obj.toString());

            try {

                HttpService.postJSON(getContext(), "/api/data/v1/row/find", token, obj, new JsonHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                final JSONArray items = response.getJSONArray("results");

                                ArrayList<GrupoProducto> ls = new ArrayList<>(items.length());
                                for (int i = 0; i < items.length(); i++) {
                                    GrupoProducto tmp = new GrupoProducto().mapFromJSONObject(items.getJSONObject(i));
                                    ls.add(tmp);
                                }

                                cb.ok(ls);

                            } else {
                                cb.err("Problemas al sincronizar");
                            }
                        } catch (JSONException e) {

                            Log.e("DBERROR", e.getMessage());
                            cb.err("Problemas al sincronizar");
                        }

                    }


                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        cb.err("Problemas al sincronizar");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadProductosPorGrupo(final String keyGrupo, final ResponseListener<ArrayList<Producto>> cb, final int page) {
        JSONObject obj = null;
        try {
            obj = new QueryBuilder(DOMAIN, "PRODUCTO", page, 1000)
                    .addField(QueryBuilder.OP.EQ, QueryBuilder.ANDOR.AND, "GRUPO", keyGrupo)
                    .compile();

            Log.d("DATA", "QUERY:" + obj.toString());

            HttpService.postJSON(getContext(), "/api/data/v1/row/find", token, obj, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {

                            final JSONArray items = response.getJSONArray("results");

                            Log.w("DATA", "FOUND:" + items.length() + " PRODUCTS");

                            ArrayList<Producto> ls = new ArrayList<>(items.length());

                            for (int i = 0; i < items.length(); i++) {
                                Producto tmp = new Producto().mapFromJSONObject(items.getJSONObject(i));
                                ls.add(tmp);
                            }
                            cb.ok(ls);

                        } else {
                            cb.err("Problemas al sincronizar");
                        }
                    } catch (JSONException e) {

                        Log.e("DBERROR", e.getMessage());
                        cb.err("Problemas al sincronizar");
                    }

                }


                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    cb.err("Problemas al sincronizar");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void loadProducto(final String keyProducto, final ResponseListener<Producto> cb) {
        JSONObject obj = null;
        try {
            obj = new QueryBuilder(DOMAIN, "PRODUCTO", 1, 1000)
                    .fetch(new String[]{"GRUPO"})
                    .compileWithKeys(new String[]{keyProducto});

            Log.d("DATA", "QUERY:" + obj.toString());

            HttpService.postJSON(getContext(), "/api/data/v1/row/find", token, obj, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    try {
                        Log.w("RESPONSE", response.toString());
                        if (response.getBoolean("success")) {
                            JSONArray items = response.getJSONArray("results");
                            if (items.getJSONObject(0) == null){
                                cb.err("Producto no existe");
                            }
                            JSONObject fetch = response.getJSONObject("fetched_results");
                            JSONObject obj = items.getJSONObject(0);

                            Producto tmp = new Producto().mapFromJSONObject(obj);
                            GrupoProducto aux = new GrupoProducto().mapFromJSONObject(
                                    fetch.getJSONObject("GRUPO").getJSONObject(tmp.getGrupo())
                            );
                            tmp.setGrupoReference(aux);

                            cb.ok(tmp);

                        } else {
                            cb.err(response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        Log.e("DBERROR", e.getMessage());
                        cb.err(e.getMessage());
                    }

                }


                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    cb.err("Problemas al sincronizar");
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void addProducto(String name, String desc, String key, final ResponseListener<Void> cb){
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


    public void updateProducto(Producto prod, final ResponseListener<Void> cb) {
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

    public void deleteProducto(Producto prod, final ResponseListener<Void> cb) {
        JSONObject obj = null;
        try {
            obj = new QueryBuilder(DOMAIN, "PRODUCTO")
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
