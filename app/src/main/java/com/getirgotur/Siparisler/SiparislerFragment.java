package com.getirgotur.Siparisler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getirgotur.AnaSayfa.SpacesItemDecoration;
import com.getirgotur.Kullanici;
import com.getirgotur.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by halilmac on 18/02/2018.
 */

public class SiparislerFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<Siparis> listSiparisler ;
    private Kullanici kullanici;
    private AdapterSiparisler adapter;
    private RecyclerView recyclerView;
    private String islem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.siparisler_fragment,container,false);

        if(getArguments().getSerializable("kullanici") != null){
            kullanici = (Kullanici) getArguments().getSerializable("kullanici");
            islem = getArguments().getString("islem");
        }
        listSiparisler = new ArrayList<>();
        adapter = new AdapterSiparisler(getActivity(), listSiparisler , islem.equals("Satilanlar") ? true : false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(islem).child(kullanici.getId());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listSiparisler.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    System.out.println("islem "+snapshot);
                    Siparis siparis = snapshot.getValue(Siparis.class);
                    listSiparisler.add(siparis);
                }
                System.out.println("Siparis : "+listSiparisler.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}
