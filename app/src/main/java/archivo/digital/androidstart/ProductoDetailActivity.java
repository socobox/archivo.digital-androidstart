package archivo.digital.androidstart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.services.DataService;
import archivo.digital.androidstart.utils.ResponseListener;

/**
 * @author http://www.socobox.co
 *         Created by miguel on 9/06/2016.
 */
public class ProductoDetailActivity extends ProductPlaceholderActivity implements View.OnClickListener {

    private TextView txt_name, txt_desc, txt_grupo;
    private Button btn_edit, btn_delete;
    Producto mProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detail);
        setTitle("Producto");
        initUI();
        loadProduct();
    }

    private void loadProduct() {
        DataService.getInstance(this).loadProducto(getIntent().getStringExtra(PRODUCTO_KEY), new ResponseListener<Producto>() {
            @Override
            public void ok(Producto obj) {
                setInfo(obj);
            }

            @Override
            public void err(String msg) {

            }
        });
    }


    private void setInfo(Producto producto) {
        mProducto = producto;
        txt_name.setText(producto.getNombre());
        txt_desc.setText(producto.getDescription());
        txt_grupo.setText(producto.getGrupoReference().getNombre());
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }

    private void initUI() {
        txt_name = (TextView) findViewById(R.id.txt_producto_name);
        txt_desc = (TextView) findViewById(R.id.txt_producto_desc);
        txt_grupo = (TextView) findViewById(R.id.txt_producto_grupo);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit:
                showModalProducto(mProducto, new ResponseListener<Producto>() {
                    @Override
                    public void ok(Producto obj) {
                        updateProducto(obj, new ResponseListener<Void>() {
                            @Override
                            public void ok(Void obj) {
                                loadProduct();
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
            case R.id.btn_delete:
                showConfirm("Estas seguro que deseas eliminar este Producto.", new ResponseListener<Void>() {
                    @Override
                    public void ok(Void obj) {
                        deleteProducto(mProducto, new ResponseListener<Void>() {
                            @Override
                            public void ok(Void obj) {
                                self.finish();
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
}
