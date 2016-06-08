package archivo.digital.androidstart;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import archivo.digital.androidstart.services.DataService;

/**
 * @author https://archivo.digital
 *         Created by miguel@archivo.digital 8/06/2016.
 */
public class PlaceHolderActivity extends AppCompatActivity {

    protected static final String GRUPO_KEY = "grupo_key";
    protected static final String GRUPO_NAME = "grupo_name";
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
}
