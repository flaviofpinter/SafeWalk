package com.example.testetcc;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testetcc.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

//    LinearLayout linearLayoutSecundario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void buscarInformacoesGPS(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        LocationManager mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        LocationManager mLocManager = (LocationManager) getSystemService();
        LocationListener mLocListener = new MinhaLocalizacaoListener();

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String texto = "Latitude.: " + MinhaLocalizacaoListener.latitude + "\n" +
                    "Longitude: " + MinhaLocalizacaoListener.longitude + "\n";
            Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show();
        }

//        this.mostrarGoogleMaps(MinhaLocalizacaoListener.latitude, MinhaLocalizacaoListener.longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            // O fragmento do mapa não existe

            // Cria o fragmento do mapa
            mapFragment = SupportMapFragment.newInstance();

            // Adiciona o fragmento do mapa ao layout
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        System.out.println("VAI TOMAR NO CU UNIP");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            DAO dao = new DAO();
            List<String> ruas = dao.getRuas();
//            LatLng[] arr = new LatLng[] {};
            HashMap<LatLng, LatLng> hashMap = new HashMap<>();
//            List<LatLng> arr = new LinkedList<LatLng>();
            for(int i = 0; i < ruas.size(); i++){
                String addressPaulista1 = ruas.get(i) + " 001, São Paulo, SP";
                String addressPaulista2 = ruas.get(i) + " 250, São Paulo, SP";

                Geocoder geocoder = new Geocoder(this);
                List<Address> addressesP1 = geocoder.getFromLocationName(addressPaulista1, 1);
                List<Address> addressesP2 = geocoder.getFromLocationName(addressPaulista2, 1);

                hashMap.put(new LatLng(addressesP2.get(0).getLatitude(), addressesP2.get(0).getLongitude()), new LatLng(addressesP1.get(0).getLatitude(), addressesP1.get(0).getLongitude()));
//                arr.add(new LatLng(addressesP1.get(0).getLatitude(), addressesP1.get(0).getLongitude()));
//                arr.add(new LatLng(addressesP2.get(0).getLatitude(), addressesP2.get(0).getLongitude()));
            }
            System.out.println(hashMap);
            String addressPaulista1 = "Avenida Paulista 001, São Paulo, SP";
            String addressPaulista2 = "Avenida Paulista 2500, São Paulo, SP";


            // Cria um objeto Geocoder
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressesP1 = geocoder.getFromLocationName(addressPaulista1, 1);
            List<Address> addressesP2 = geocoder.getFromLocationName(addressPaulista2, 1);

            // Se a conversão foi bem-sucedida, obtem as coordenadas do começo da rua
            if (addressesP1.size() > 0 && addressesP2.size() > 0) {
                Address address1 = addressesP1.get(0);
                LatLng start = new LatLng(address1.getLatitude(), address1.getLongitude());

                // Obtém as coordenadas do fim da rua
//                Address address2 = addressesP2.get(addressesP2.size() - 1);
                Address address2 = addressesP2.get(0);
                LatLng end = new LatLng(address2.getLatitude(), address2.getLongitude());



                // Add polylines to the map.
                // Polylines are useful to show a route or some other connection between points.
                for (HashMap.Entry<LatLng, LatLng> entry : hashMap.entrySet()) {
                    Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .add(entry.getKey(), entry.getValue()));
                    polyline1.setTag("A");
                }
//                System.out.println(start);
//                System.out.println(end);
                // Store a data object with the polyline, used here to indicate an arbitrary type.

            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        // Endereço da rua que você deseja obter as coordenadas


        // Tenta converter o endereço em coordenadas

//        Polyline polyline2 = googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-23.568952176077044, -46.647165094873216),
//                        new LatLng(-23.556417773933546, -46.662359700269214)));
//        polyline2.setTag("B");
//        stylePolyline(polyline2);

        // Add polygons to indicate areas on the map.
//        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-27.457, 153.040),
//                        new LatLng(-33.852, 151.211),
//                        new LatLng(-37.813, 144.962),
//                        new LatLng(-34.928, 138.599)));
//        // Store a data object with the polygon, used here to indicate an arbitrary type.
//        polygon1.setTag("alpha");
//        // Style the polygon.
//        stylePolygon(polygon1);
//
//        Polygon polygon2 = googleMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-31.673, 128.892),
//                        new LatLng(-31.952, 115.857),
//                        new LatLng(-17.785, 122.258),
//                        new LatLng(-12.4258, 130.7932)));
//        polygon2.setTag("beta");
//        stylePolygon(polygon2);

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    /**
     * Styles the polyline, based on type.
     *
     * @param polyline The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    /**
     * Listens for clicks on a polyline.
     *
     * @param polyline The polyline object that the user has clicked.
     */
    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Listens for clicks on a polygon.
     *
     * @param polygon The polygon object that the user has clicked.
     */
    @Override
    public void onPolygonClick(Polygon polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(color);

        Toast.makeText(this, "Area type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_DARK_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_LIGHT_GREEN_ARGB = 0xff81C784;
    private static final int COLOR_DARK_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_LIGHT_ORANGE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    public void mostrarGoogleMaps(double latitude, double longitude) {
        WebView wv = findViewById(R.id.map);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }
}