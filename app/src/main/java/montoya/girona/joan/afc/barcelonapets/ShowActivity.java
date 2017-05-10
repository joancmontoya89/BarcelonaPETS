package montoya.girona.joan.afc.barcelonapets;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class ShowActivity extends AppCompatActivity {

    private final static String LOG_TAG = ShowActivity.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_show);
        Bundle args = new Bundle();
        args.putString("choosenAnimal", getIntent().getExtras().getString("choosenAnimal"));

        ShowActivityFragment fragment = ShowActivityFragment.newInstance(null);
        fragment.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, fragment, ShowActivityFragment.LOG_TAG);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
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
        else if (id == R.id.action_lost_dogs) {
            //TODO go to the fragment to see all lost dogs
            //TODO create the fragment to see all lost dogs
        }
        else if (id == R.id.action_lost_cats) {
            //TODO go to the fragment to see all lost cats
            //TODO create the fragment to see all lost cats
        }
        else if (id == R.id.action_street_dog) {
            //TODO go to the fragment to upload a dog you have found in the street
            //TODO create the fragment  to upload a dog you have found in the street
        }
        else if (id == R.id.action_street_cat) {
            //TODO go to the fragment to upload a cat you have found in the street
            //TODO create the fragment to upload a cat you have found in the street
        }

        return super.onOptionsItemSelected(item);
    }
}
