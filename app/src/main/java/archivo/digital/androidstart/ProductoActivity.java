package archivo.digital.androidstart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import archivo.digital.android.ADCallback;
import archivo.digital.androidstart.R;
import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.services.DataService;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class ProductoActivity extends ProductPlaceholderActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView mListView;
    ArrayAdapter mAdapter;
    ArrayList<String> mList;
    ArrayList<Producto> mListMaster;
    private String[] options = {"Editar", "Borrar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        setTitle(getIntent().getStringExtra(GRUPO_NAME));
        mListMaster = new ArrayList<>();
        mList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list_productos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadProducts();
    }

    private void loadProducts() {
        DataService.getInstance(this).loadProductosPorGrupo(getIntent().getStringExtra(GRUPO_KEY), new ADCallback<List<Producto>>() {
            @Override
            public void ok(List<Producto> obj) {
                mList.clear();
                mListMaster.clear();
                mListMaster.addAll(obj);
                for (Producto g : obj) {
                    mList.add(g.toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void err(String msg) {

            }
        }, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            final Producto prod = new Producto();
            prod.setGrupo(getIntent().getStringExtra(GRUPO_KEY));
            showModalProducto(prod, new ADCallback<Producto>() {
                @Override
                public void ok(Producto obj) {
                    addProducto(obj, new ADCallback<Void>() {
                        @Override
                        public void ok(Void obj) {
                            loadProducts();
                        }

                        @Override
                        public void err(String msg) {

                        }
                    });
                }

                @Override
                public void err(String msg) {

                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Producto prod = mListMaster.get(position);
        Intent intent = new Intent(self, ProductoDetailActivity.class);
        intent.putExtra(PRODUCTO_KEY, prod.getKey());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Producto prod = mListMaster.get(position);
        showOptions(options, new ADCallback<Integer>() {
            @Override
            public void ok(Integer obj) {
                switch (obj) {
                    case 0:
                        showModalProducto(prod, new ADCallback<Producto>() {
                            @Override
                            public void ok(Producto obj) {
                                updateProducto(obj, new ADCallback<Void>() {
                                    @Override
                                    public void ok(Void obj) {
                                        loadProducts();
                                    }

                                    @Override
                                    public void err(String msg) {

                                    }
                                });
                            }

                            @Override
                            public void err(String msg) {

                            }
                        });
                        break;
                    case 1:
                        showConfirm("Esta seguro que deseas eliminar este producto.", new ADCallback<Void>() {
                            @Override
                            public void ok(Void obj) {
                                deleteProducto(prod, new ADCallback<Void>() {
                                    @Override
                                    public void ok(Void obj) {
                                        loadProducts();
                                    }

                                    @Override
                                    public void err(String msg) {

                                    }
                                });
                            }

                            @Override
                            public void err(String msg) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void err(String msg) {

            }
        });
        return true;
    }



}
