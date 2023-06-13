package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import br.com.testesbottomnav.FragmentsAdapterApostas;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.Usuario;
import br.com.testesbottomnav.util.UsuarioLogado;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Apostas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Apostas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private FirebaseFirestore db;
    private DocumentReference usuariosRef;

    private TextView saldo;
    private EditText editTextValor;
    private Button botaoDepositar;
    private FragmentsAdapterApostas fragmentsAdapterApostas;

    public Apostas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Apostas.
     */
    // TODO: Rename and change types and number of parameters
    public static Apostas newInstance(String param1, String param2) {
        Apostas fragment = new Apostas();
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
        return inflater.inflate(R.layout.fragment_apostas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize(view);

        tabLayout = view.findViewById(R.id.tab_Layout_apostas);
        viewPager2 = view.findViewById(R.id.pager2_apostas);

        tabLayout.addTab(tabLayout.newTab().setText("Conta"));
        tabLayout.addTab(tabLayout.newTab().setText("Apostas"));
        tabLayout.addTab(tabLayout.newTab().setText("Historico"));

        FragmentManager fragmentManager = getChildFragmentManager();

        fragmentsAdapterApostas = new FragmentsAdapterApostas(fragmentManager, getLifecycle());

        viewPager2.setAdapter(fragmentsAdapterApostas);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    private void depositarSaldo(Map<String, Object> map) {
        usuariosRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Saldo registrado", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Não foi possivel registrar o saldo: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dataInitialize(View view) {
        editTextValor = view.findViewById(R.id.valor_a_depositar);
        saldo = view.findViewById(R.id.saldo_disponivel);
        botaoDepositar = view.findViewById(R.id.botaoDepositar);
        db = FirebaseFirestore.getInstance();
        usuariosRef = db.collection("usuarios").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        usuariosRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getContext(), "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null && value.exists()) {

                    Map<String, Object> dados = value.getData();
                    if(dados.get("saldo").toString() != null){
                        saldo.setText(dados.get("saldo").toString());
                    }
                }
            }
        });

        botaoDepositar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double saldoSomado = 0.0;
                if (!editTextValor.getText().toString().equals("")){
                    Double valor = Double.parseDouble(editTextValor.getText().toString());
                    Double saldoAtual = Double.parseDouble(UsuarioLogado.userLogado.getSaldo());

                    if (valor != null && saldoAtual != null) {
                        saldoSomado = valor + saldoAtual;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("saldo", saldoSomado.toString());
                    depositarSaldo(map);
                }

            }
        });
    }


}