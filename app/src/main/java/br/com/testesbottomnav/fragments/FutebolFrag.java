package br.com.testesbottomnav.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.testesbottomnav.Adapter;
import br.com.testesbottomnav.AreaApostas;
import br.com.testesbottomnav.Item;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.TesteDeFuncionalidadeRemoverDepois;
import br.com.testesbottomnav.model.PartidaModelDb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FutebolFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FutebolFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Item> items;
    private RecyclerView recyclerView;
    private DatabaseReference pDatabaseReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FutebolFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FutebolFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FutebolFrag newInstance(String param1, String param2) {
        FutebolFrag fragment = new FutebolFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_futebol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pDatabaseReference = FirebaseDatabase.getInstance().getReference("partidas");
        items = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view_futebol);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    PartidaModelDb modelDb = postSnapshot.getValue(PartidaModelDb.class);
                    if (modelDb.getLiga().equals("Brasileirão")){
                        items.add(new Item(modelDb.getNomeTimeA(), modelDb.getNomeTimeB(), modelDb.getHorario()
                        , modelDb.getData(), modelDb.getTimeAimageUrl(), modelDb.getTimeBimageUrl()));
                    }
                }

                Adapter adapter = new Adapter(getContext(), items, new Adapter.OnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Item item = items.get(position);
                        Intent intent = new Intent(getContext(), AreaApostas.class);
                        intent.putExtra("partida", item);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}