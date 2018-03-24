package info.blogbasbas.wisatatraining;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.blogbasbas.wisatatraining.db.model.WisataItem;
import info.blogbasbas.wisatatraining.helper.Constant;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {


    public static final String PARAMETERTBL = "WISATA";
    @BindView(R.id.iv_detail_gambar)
    ImageView ivDetailGambar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tv_detail_alamat)
    TextView tvDetailAlamat;
    @BindView(R.id.tv_detail_deskripsi)
    TextView tvDetailDeskripsi;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tvEventWisata)
    TextView tvEventWisata;
    private Gson data = new Gson();
    private WisataItem wisataItem;
    String longWisata, latWisata;
    String getDataIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataIntent = getIntent().getStringExtra(PARAMETERTBL);
        wisataItem = data.fromJson(getDataIntent, WisataItem.class);

        Timber.e("Hasil Data :" + wisataItem.getNamaWisata());
        Timber.e("Hasil Data :" + wisataItem.getGambarWisata());


        getSupportActionBar().setTitle(wisataItem.getNamaWisata());
        tvDetailDeskripsi.setText(wisataItem.getDeksripsiWisata());
        tvDetailAlamat.setText(wisataItem.getAlamatWisata());
        tvEventWisata.setText(wisataItem.getEventWisata());
        Picasso.get().load(Constant.URL_IMAGE+wisataItem.getGambarWisata())
                .error(R.drawable.noimage)
                .placeholder(R.drawable.noimage)
                .into(ivDetailGambar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                route();

            }
        });
    }
        void route(){
        try {

         longWisata = wisataItem.getLongitudeWisata();
            latWisata = wisataItem.getLatitudeWisata();
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latWisata + "," + longWisata + "");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (Exception e){
            Timber.e("Error" +e.getMessage());
        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    //back tombol
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        } else if (i == R.id.action_rute){
            route();
        }
        return super.onOptionsItemSelected(item);

    }
}
