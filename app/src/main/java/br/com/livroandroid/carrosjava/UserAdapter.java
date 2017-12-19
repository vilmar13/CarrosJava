package br.com.livroandroid.carrosjava;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    private final Activity activity;
    private List<Carro> carroList;

    public UserAdapter(Activity activity, ImageLoader imageLoader){
        this.activity = activity;
        this.carroList = new ArrayList<>();
        this.imageLoader = imageLoader;
    }

    public void addNewList(List<Carro> list){
        carroList.clear();
        addList(list);
    }

    public void addList(List<Carro> list){
        carroList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return carroList.size();
    }

    @Override
    public Object getItem(int position) {
        return carroList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.list_cell, parent, false);
        Carro carro = carroList.get(position);

        TextView txtId =(TextView) view.findViewById(R.id.txtId);
        TextView txtTitle =(TextView) view.findViewById(R.id.txtName);
        TextView txtValue =(TextView) view.findViewById(R.id.txtValue);

        txtId.setText(String.valueOf(carro.getId()));
        txtTitle.setText(carro.getName());
        txtValue.setText(carro.getDesc());

        NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.img);
        imageView.setImageUrl(carro.getImage(), imageLoader);

        return view;
    }


}
