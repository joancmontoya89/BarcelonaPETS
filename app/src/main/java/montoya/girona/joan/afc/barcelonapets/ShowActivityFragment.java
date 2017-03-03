package montoya.girona.joan.afc.barcelonapets;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A placeholder fragment containing a simple view.
 */
public class ShowActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ShowActivityFragment.class.getSimpleName();
    private Calendar calendar;
    private int month_i;
    private static final String[] ANIMAL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            Contract.TABLE_ANIMALS + "." + Contract.Animal.COLUMN_ID,
            Contract.Animal.COLUMN_RECDOGS,
            Contract.Animal.COLUMN_RESDOGS,
            Contract.Animal.COLUMN_ADODOGS,
            Contract.Animal.COLUMN_RECCATS,
            Contract.Animal.COLUMN_RESCATS,
            Contract.Animal.COLUMN_ADOCATS
    };

    AnimalAdapter animalAdapter;

    //URL of csv:
    //http://opendata.bcn.cat/opendata/ca/descarrega-fitxer?url=http%3a%2f%2fbismartopendata.blob.core.windows.net%2fopendata%2fopendata%2f2015_ACOLLIDA_ANIMALS2015.csv&name=ACOLLIDA_ANIMALS2015.csv
    private TextView tvShowTittle;
    private Spinner spSelectMonth;
    private ListView lvAnimalStatistics;
    private ArrayList months;
    private ArrayList<ArrayList<String>> dogsData;
    private ArrayList<ArrayList<String>> catsData;
    private String choosenAnimal;

    public static ShowActivityFragment newInstance(Bundle arguments) {
        ShowActivityFragment f = new ShowActivityFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    public ShowActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show, container, false);

        tvShowTittle = (TextView)root.findViewById(R.id.textViewShow);
        spSelectMonth = (Spinner)root.findViewById(R.id.spinnerMonth);
        lvAnimalStatistics = (ListView)root.findViewById(R.id.listViewAnimals);
        dogsData = new ArrayList<ArrayList<String>>(3);
        catsData = new ArrayList<ArrayList<String>>(3);

        calendar = Calendar.getInstance();
        month_i = calendar.getTime().getMonth();

        animalAdapter = new AnimalAdapter(getActivity(), null, 0);

        // getting choosen animal
        Bundle args = getArguments();
        choosenAnimal = args.getString("choosenAnimal");
        if (choosenAnimal.contentEquals("dog")) {
            tvShowTittle.setText("Dog statistics");
            tvShowTittle.setTextColor(Color.rgb(51, 51, 255));
            spSelectMonth.setBackgroundColor(Color.argb(95, 102, 178, 255));
            //Log.i(LOG_TAG, "Animal triat: " + choosenAnimal + "\n\n\n\n");
        }
        else {
            //cat is the choosen animal
            tvShowTittle.setText("Cat statistics");
            tvShowTittle.setTextColor(Color.rgb(0, 153, 0));
            spSelectMonth.setBackgroundColor(Color.argb(95, 0, 153, 0));
            //Log.i(LOG_TAG, "Animal triat: " + choosenAnimal + "\n\n\n\n");
        }

        //spinner's data and charge
        String[] months_aux = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December"};
        months = new ArrayList(12);
        for (int i = 0; i < month_i; i++) {
            months.add(months_aux[i]);
        }
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, months);
        spSelectMonth.setAdapter(spAdapter);

        //fetch animals data, recollected, rescued and adopted
        FetchAnimalsDataTask animalsTask = new FetchAnimalsDataTask();
        animalsTask.execute();

        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {
            Log.i(LOG_TAG, "Error esperant 1s.");
        }

        spSelectMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = spSelectMonth.getSelectedItemPosition();
                Log.i(LOG_TAG, "month_i = " + month_i);
                Log.i(LOG_TAG, "pos_seleccionada = " + pos);
                if (pos > month_i - 1) {
                    String toast_text = "No such data available yet";
                    Toast.makeText(getActivity(), toast_text, Toast.LENGTH_SHORT).show();
                    return;
                }

                while (dogsData.isEmpty()) {
                    try {
                        //waiting for a second
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        Log.i(LOG_TAG, "Error while waiting for a second :(");
                    }
                }

                //listView adapter and charge
                String[] arrayWithAnimalData = new String[3];
                if (choosenAnimal.contentEquals("dog")) { //dog choosed
                    arrayWithAnimalData[0] = (dogsData.get(0)).get(pos) + " recollected";    //recollected January
                    arrayWithAnimalData[1] = (dogsData.get(1)).get(pos) + " rescued";        //rescued
                    arrayWithAnimalData[2] = (dogsData.get(2)).get(pos) + " adoptated";      //adoptated
                }
                else {
                    arrayWithAnimalData[0] = (catsData.get(0)).get(pos) + " recollected";    //recollected January
                    arrayWithAnimalData[1] = (catsData.get(1)).get(pos) + " rescued";        //rescued
                    arrayWithAnimalData[2] = (catsData.get(2)).get(pos) + " adoptated";      //adoptated
                }

                for (String s:arrayWithAnimalData) {
                    Log.i(LOG_TAG, "informacio amb la data que s'hauria de mostrar:\n\t" + s);
                }
                ArrayAdapter<String> lvAdapter;
                lvAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayWithAnimalData);
                lvAnimalStatistics.setAdapter(lvAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by date.
        String sortOrder = Contract.Animal.COLUMN_ID + " ASC";
        Uri monthUri = Contract.Animal.buildMonthUri(month_i - 1); //1 <= month_i <= 12

        return new CursorLoader(getActivity(),
                monthUri,
                ANIMAL_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        animalAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        animalAdapter.swapCursor(null);
    }


    public class AnimalAdapter extends CursorAdapter {
        public AnimalAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        /*
            Remember that these views are reused as needed.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);

            return view;
        }

        /*
            This is where we fill-in the views with the contents of the cursor.
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // our view is pretty simple here --- just a text view
            // we'll keep the UI functional with a simple (and slow!) binding.

            //TextView tv = (TextView)view;
            //tv.setText(convertCursorRowToUXFormat(cursor));
        }
    }


    public class FetchAnimalsDataTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchAnimalsDataTask.class.getSimpleName();
        //private ArrayList<ArrayList<String>> dogsData;
        //private ArrayList<ArrayList<String>> catsData;

        @Override
        protected String[] doInBackground(String... params) {

            //dogsData = new ArrayList<ArrayList<String>>(3);
            //catsData = new ArrayList<ArrayList<String>>(3);

            try {
                URL dadesAnimals = new URL("http://opendata.bcn.cat/opendata/ca/descarrega-fitxer?url=http%3a%2f%2fbismartopendata.blob.core.windows.net%2fopendata%2fopendata%2f2015_ACOLLIDA_ANIMALS2015.csv&name=ACOLLIDA_ANIMALS2015.csv");
                HttpURLConnection conexio = (HttpURLConnection) dadesAnimals.openConnection();
                conexio.setRequestMethod("GET");
                conexio.connect();
                InputStream is = conexio.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                line = br.readLine();
                String elem = "";
                ArrayList<ContentValues> cvArrayList = new ArrayList<>(6);

                for (int i = 1; i < 7; i++) {       //File has 7 lines, let's look dog's & cat's
                    ArrayList<String> dades_i = new ArrayList<String>();
                    line = br.readLine();
                    int trobats = 1;                //nº of found commas

                    String[] animals = line.split(",");

                    while (trobats < animals.length) {
                        dades_i.add(animals[trobats]);
                        trobats++;
                    }
                    dades_i.add(animals[0]);

                    if (i < 4 && (!dades_i.isEmpty())) {
                        dogsData.add(new ArrayList<String>(dades_i));
                        //Log.d(LOG_TAG, "info dog:\n\t" + dades_i.toString());
                    }
                    else if (!dades_i.toString().isEmpty()) {
                        catsData.add(new ArrayList<String>(dades_i));
                    }
                }

                //To fill content provider needed data
                for (int i = 0; i < dogsData.size(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(Contract.Animal.COLUMN_ID, i);
                    cv.put(Contract.Animal.COLUMN_RECDOGS, (dogsData.get(i)).get(0));
                    cv.put(Contract.Animal.COLUMN_RESDOGS, (dogsData.get(i)).get(1));
                    cv.put(Contract.Animal.COLUMN_ADODOGS, (dogsData.get(i)).get(2));
                    cv.put(Contract.Animal.COLUMN_RECCATS, (catsData.get(i)).get(0));
                    cv.put(Contract.Animal.COLUMN_RESCATS, (catsData.get(i)).get(1));
                    cv.put(Contract.Animal.COLUMN_ADOCATS, (catsData.get(i)).get(2));
                    cvArrayList.add(cv);
                }

                // add to database
                if ( cvArrayList.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cvArrayList.size()];
                    cvArrayList.toArray(cvArray);
                }
            }
            catch (MalformedURLException e) {
                Log.d(LOG_TAG, e.getMessage().toString());
            }
            catch (IOException e) {
                Log.d(LOG_TAG, "Error connexio:\n\t" + e.getMessage().toString());
            }
            catch (Exception e) {
                Log.d(LOG_TAG, "Altra excepció produida\n\t" + e.getMessage());
            }

            return null;
        }
    }
}
