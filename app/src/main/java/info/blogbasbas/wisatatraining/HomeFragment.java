package info.blogbasbas.wisatatraining;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.infideap.atomic.Atom;
import com.infideap.atomic.FutureCallback;

import org.greenrobot.greendao.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import info.blogbasbas.wisatatraining.adapter.AdapterWisata;
import info.blogbasbas.wisatatraining.db.facade.Facade;
import info.blogbasbas.wisatatraining.db.facade.ManageWisataTbl;
import info.blogbasbas.wisatatraining.db.model.DaoSession;
import info.blogbasbas.wisatatraining.db.model.ResponseWisata;
import info.blogbasbas.wisatatraining.db.model.WisataItem;
import info.blogbasbas.wisatatraining.db.model.WisataItemC;
import info.blogbasbas.wisatatraining.db.model.WisataItemDao;
import info.blogbasbas.wisatatraining.helper.Constant;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    DaoSession daoSession;
    WisataItemDao wisataItemDao;
    private Query<WisataItem> wisataItemQuery;
    AdapterWisata adapterWisata;
    RecyclerView recyclerView;
    List<WisataItem> itemList;
    List<WisataItem> wisataItems;
    ManageWisataTbl manageWisataTbl;
    ImageView imgRefresh;
     ProgressDialog mProgressDialog;



    Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvWisata);

        unbinder = ButterKnife.bind(this, view);

        manageWisataTbl = Facade.getInstance().getManageWisataTbl();
        wisataItems = new ArrayList<>();


        daoSession = BaseApp.getDaoSession();
        wisataItemDao  = daoSession.getWisataItemDao();
        itemList = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Loading Data");
        mProgressDialog.setMessage("Please wait....");
        mProgressDialog.show();

        getData();
        setUpView();
        wisataItemQuery = wisataItemDao.queryBuilder().orderAsc(WisataItemDao.Properties.IdWisata).build();
        updateView();

        return view;
    }

    private void getData() {



        Atom.with(getActivity())
                // .load("https://reqres.in/api/users/2")
                // .load("https://wisata-smg-basri.000webhostapp.com/wisata_semarang/wisata_semarang/wisata/read_wisata.php")
                .load(Constant.URL)
                .as(ResponseWisata.class)
                .setCallback(new FutureCallback<ResponseWisata>() {
                    @Override
                    public void onCompleted(Exception e, ResponseWisata result) {
                        if (e != null)
                            Log.e("", "e : " + new Gson().toJson(e));
                        Timber.e("Hasil :"+result);
                        Log.e("", "onCompleted: " + result);

                        if (result.isSuccess()== true){
                            List<WisataItem> wisataItems = result.getWisata();

                            final WisataItem wisataItem = new WisataItem();
                            if (wisataItem != null) {
                                manageWisataTbl.removeAll();
                                manageWisataTbl.add(wisataItems);
                            }

                        }

                        mProgressDialog.dismiss();

                    }
                });



    }

    private void updateView() {
        List<WisataItem> wisataItems = wisataItemQuery.list();
        adapterWisata.setWisata(wisataItems);

    }

    private void setUpView() {
        adapterWisata = new AdapterWisata(cliclistiner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressDialog.dismiss();
        recyclerView.setAdapter(adapterWisata);

    }

    AdapterWisata.ClickListener cliclistiner = new AdapterWisata.ClickListener() {
        @Override
        public void onClick(int position) {
            WisataItem wisataItem = adapterWisata.getWisatalItem(position);
            String wisataItemNama = wisataItem.getNamaWisata();
            Toast.makeText(getActivity(), "Memilih : "+wisataItemNama, Toast.LENGTH_SHORT).show();
            Intent kirimData = new Intent(getActivity(),DetailActivity.class);
            kirimData.putExtra(DetailActivity.PARAMETERTBL, new Gson().toJson(wisataItem));
            startActivity(kirimData);

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
