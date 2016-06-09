package archivo.digital.androidstart;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import archivo.digital.androidstart.models.Producto;
import archivo.digital.androidstart.services.DataService;
import archivo.digital.androidstart.utils.ResponseListener;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class PlaceHolderActivity extends AppCompatActivity {

    protected static final String GRUPO_KEY = "grupo_key";
    protected static final String GRUPO_NAME = "grupo_name";
    protected static final String PRODUCTO_KEY = "producto_key";
    protected Activity self;
    protected static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        DataService.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (self == null)
            self = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    protected void goBack() {
        this.finish();
    }

    public void setToolbarTitle(String title){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setToolbarSubtitle(String subtitle){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    public void setSubtitle(String subtitle){
        if (getActionBar() != null) {
            getActionBar().setSubtitle(subtitle);
        }
    }


    protected void setBackButton(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showConfirm(String msg, final ResponseListener<Void> cb) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cb.ok(null);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cb.err("Cancel");
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    protected void showOptions(String[] options, final ResponseListener<Integer> cb) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Optiones");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cb.ok(which);
            }
        });
        builder.setNegativeButton(getResources().getText(R.string.cancel), null);
        Dialog dialog = builder.create();
        dialog.show();
    }
}
