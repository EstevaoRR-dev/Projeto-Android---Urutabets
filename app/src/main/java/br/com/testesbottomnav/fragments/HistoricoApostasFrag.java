package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.testesbottomnav.ApostasEmAbertoAdapter;
import br.com.testesbottomnav.ItemApostasEmAberto;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.ApostaModelDb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoricoApostasFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricoApostasFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private List<ItemApostasEmAberto> items;
    private FirebaseFirestore db;
    private CollectionReference apostasRef;
    private Query query;
    private String userId;

    public HistoricoApostasFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoricoApostasFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricoApostasFrag newInstance(String param1, String param2) {
        HistoricoApostasFrag fragment = new HistoricoApostasFrag();
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
        return inflater.inflate(R.layout.fragment_historico_apostas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize(view);

    }

    private void dataInitialize(View view){
        items = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.recyclerView_historico_apostas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        apostasRef = db.collection("apostas");
        query = apostasRef.whereEqualTo("userId", userId).whereEqualTo("estado", "fechado");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ApostaModelDb> apostas = new ArrayList<>();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    ApostaModelDb aposta = documentSnapshot.toObject(ApostaModelDb.class);
                    apostas.add(aposta);
                }

                ApostasEmAbertoAdapter adapter = new ApostasEmAbertoAdapter(getContext(), getListApostas(apostas));
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Falha ao carregar as apostas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }


    private List<ItemApostasEmAberto> getListApostas(List<ApostaModelDb> apostas){
        List<ItemApostasEmAberto> apostasRecycler = new ArrayList<>();

        for(ApostaModelDb aposta : apostas){
            apostasRecycler.add(new ItemApostasEmAberto(aposta.getLogoTimeaUrl(), aposta.getLogoTimebUrl(), aposta.getApostas()
                    , aposta.getValorAposta(),aposta.getEstado(), aposta.getResultado()));
        }

        return apostasRecycler;
    }

}