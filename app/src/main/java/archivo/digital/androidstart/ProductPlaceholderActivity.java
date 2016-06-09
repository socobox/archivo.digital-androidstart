package archivo.digital.androidstart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.services.DataService;
import archivo.digital.androidstart.utils.ResponseListener;

/**
 * @author http://www.socobox.co
 *         Created by miguel on 9/06/2016.
 */
public class ProductPlaceholderActivity extends PlaceHolderActivity {
    private EditText et_name, et_desc;

    public void showModalProducto(final Producto prod, final ResponseListener<Producto> cb) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.modal_producto, null);
        setViewPayment(view);
        et_name.setText(prod.getNombre());
        et_desc.setText(prod.getDescription());

        builder.setView(view);
        builder.setTitle(prod.getKey() == null ? "Nuevo Producto" : "Editar Producto");
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = et_name.getText().toString();
                String desc = et_desc.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(self, "El nombre del producto no puede estar vacio.", Toast.LENGTH_SHORT).show();
                    showModalProducto(prod, cb);
                    return;
                }
                prod.setNombre(name);
                prod.setDescription(desc);
                cb.ok(prod);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void setViewPayment(View view) {
        et_name = (EditText) view.findViewById(R.id.et_product_name);
        et_desc = (EditText) view.findViewById(R.id.et_product_desc);
    }

    public void addProducto(Producto prod, final ResponseListener<Void> cb) {
        String name = prod.getNombre(), desc = prod.getDescription(), keyGrupo = prod.getGrupo();
        DataService.getInstance(this).addProducto(name, desc, keyGrupo, new ResponseListener<Void>() {
            @Override
            public void ok(Void obj) {
                cb.ok(null);
            }

            @Override
            public void err(String msg) {
                Toast.makeText(self, "Error al Guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProducto(Producto prod, final ResponseListener<Void> cb) {
        DataService.getInstance(this).updateProducto(prod, new ResponseListener<Void>() {
            @Override
            public void ok(Void obj) {
                cb.ok(null);
            }

            @Override
            public void err(String msg) {
                Toast.makeText(self, "Error al Guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteProducto(Producto prod, final ResponseListener<Void> cb) {
        DataService.getInstance(this).deleteProducto(prod, new ResponseListener<Void>() {
            @Override
            public void ok(Void obj) {
                cb.ok(null);
            }

            @Override
            public void err(String msg) {
                Toast.makeText(self, "Error al Guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
