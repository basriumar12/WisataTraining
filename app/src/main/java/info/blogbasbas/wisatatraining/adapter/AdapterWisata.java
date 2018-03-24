package info.blogbasbas.wisatatraining.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import info.blogbasbas.wisatatraining.R;
import info.blogbasbas.wisatatraining.db.model.WisataItem;
import info.blogbasbas.wisatatraining.helper.Constant;
import timber.log.Timber;

/**
 * Created by User on 24/03/2018.
 */

public class AdapterWisata extends RecyclerView.Adapter<AdapterWisata.MyHolder> {
    private ClickListener clickListener;
    private List<WisataItem> dataset;

    //interface linstiner klik
    public interface ClickListener {
        void onClick (int position);
    }
    //counstructor
    public AdapterWisata(ClickListener clickListener){
        this.clickListener = clickListener;
        this.dataset = new ArrayList<WisataItem>();
    }
    public void setWisata (List<WisataItem> wisataItems){
        dataset = wisataItems;
        notifyDataSetChanged();
    }
    public WisataItem getWisatalItem(int position){
        return dataset.get(position);
    }
    @Override
    public AdapterWisata.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wisata, parent, false);
        return new MyHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(AdapterWisata.MyHolder holder, final int position) {

        //get posisition
        final WisataItem wisataItem=  dataset.get(position);
        Timber.e("get data :" +wisataItem.getNamaWisata());
        holder.tvNamaWIsata.setText(wisataItem.getNamaWisata());
        holder.tvKet.setText(wisataItem.getAlamatWisata());
        Timber.e("Url Wisata :"+wisataItem.getGambarWisata());
        Picasso.get().load(Constant.URL_IMAGE+wisataItem.getGambarWisata())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgWisata);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.e("KLIK :" +wisataItem.getIdWisata());
                clickListener.onClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imgWisata;
        TextView tvNamaWIsata, tvKet;
        public MyHolder(View itemView, final ClickListener clickListener) {
            super(itemView);
            imgWisata = (ImageView) itemView.findViewById(R.id.imgWisata);
            tvNamaWIsata = (TextView) itemView.findViewById(R.id.tvNamaWisata);
            tvKet =(TextView) itemView.findViewById(R.id.tvKet);
        }
    }
}
