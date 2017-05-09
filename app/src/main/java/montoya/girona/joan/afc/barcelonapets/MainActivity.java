package montoya.girona.joan.afc.barcelonapets;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//

public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         *
         *
         *
         */
        Context context = this.getApplicationContext();
        Signature[] sigs = new Signature[0];
        try {
            sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature sig : sigs) {
            Log.i(LOG_TAG, "Signature hashcode : " + sig.hashCode());
        }

        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method on click dog image
    public void onClickDog (View view) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("choosenAnimal", "dog");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Log.e(LOG_TAG, "Error al canviar a la ShowActivity");
        }
    }

    //Method on click dog image
    public void onClickCat (View view) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("choosenAnimal", "cat");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Log.e(LOG_TAG, "Error al canviar a la ShowActivity");
        }
    }
}
