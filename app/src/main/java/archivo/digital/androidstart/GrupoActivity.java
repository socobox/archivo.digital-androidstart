package archivo.digital.androidstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import archivo.digital.androidstart.R;
import archivo.digital.androidstart.models.GrupoProducto;
import archivo.digital.androidstart.services.DataService;
import archivo.digital.androidstart.utils.ResponseListener;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class GrupoActivity extends PlaceHolderActivity implements AdapterView.OnItemClickListener {

    ListView mListView;
    ArrayAdapter mAdapter;
    ArrayList<String> mList;
    Map<Integer, GrupoProducto> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        setTitle("Grupos de Productos");
        mList = new ArrayList<>();
        map = new HashMap<>();
        mListView = (ListView) findViewById(R.id.list_grupo);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataService.getInstance(this).loadGrupoProductos(new ResponseListener<ArrayList<GrupoProducto>>() {
            @Override
            public void ok(ArrayList<GrupoProducto> obj) {
                mList.clear();
                map.clear();
                for (int i = 0; i < obj.size(); i++) {
                    GrupoProducto g = obj.get(i);
                    map.put(i, g);
                    mList.add(g.toString());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void err(String msg) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ProductoActivity.class);
        intent.putExtra(GRUPO_KEY, map.get(position).getKey());
        intent.putExtra(GRUPO_NAME, map.get(position).getNombre());
        startActivity(intent);
    }
}
