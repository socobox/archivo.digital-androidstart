package archivo.digital.androidstart;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import archivo.digital.androidstart.R;
import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.services.DataService;
import archivo.digital.androidstart.utils.ResponseListener;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class ProductoActivity extends PlaceHolderActivity{

    ListView mListView;
    ArrayAdapter mAdapter;
    ArrayList<String> mList;
    Map<Integer, Producto> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        setTitle(getIntent().getStringExtra(GRUPO_NAME));
        mList = new ArrayList<>();
        map = new HashMap<>();
        mListView = (ListView) findViewById(R.id.list_productos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadProducts();
    }

    private void loadProducts() {
        DataService.getInstance(this).loadProductosPorGrupo(getIntent().getStringExtra(GRUPO_KEY), new ResponseListener<ArrayList<Producto>>() {
            @Override
            public void ok(ArrayList<Producto> obj) {
                mList.clear();
                map.clear();
                for (int i = 0; i < obj.size(); i++) {
                    Producto g = obj.get(i);
                    map.put(i, g);
                    mList.add(g.toString());
                }
                mAdapter.notifyDataSetChanged();
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
            Toast.makeText(this, "pendiente de implementar", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addProducto(String name, String desc){
        DataService.getInstance(this).addProducto(name, desc, getIntent().getStringExtra(GRUPO_KEY), new ResponseListener<Void>() {
            @Override
            public void ok(Void obj) {
                loadProducts();
            }

            @Override
            public void err(String msg) {
                Toast.makeText(self, "Error al Guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
