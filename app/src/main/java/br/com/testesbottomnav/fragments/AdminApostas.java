package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.ApostaModelDb;
import br.com.testesbottomnav.model.Usuario;
import br.com.testesbottomnav.util.UsuarioLogado;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminApostas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminApostas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinner;
    private ImageView nomeTimeA, nomeTimeB;
    private TextInputEditText todasAsApostas;
    private EditText valorApostado, resultado;
    private Button botaoEncerrarAposta;
    private FirebaseFirestore db;
    private CollectionReference apostasRef;
    private CollectionReference usuariosRef;
    private List<ApostaModelDb> apostaModelDbs;


    public AdminApostas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminApostas.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminApostas newInstance(String param1, String param2) {
        AdminApostas fragment = new AdminApostas();
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
        return inflater.inflate(R.layout.fragment_admin_apostas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize(view);

    }

    //Metodo vai encerrar a aposta,e atualizar o saldo do usuário.
    private void encerrarAposta(String chave, String resultado, ApostaModelDb userApostaId) {
        DocumentReference ref = apostasRef.document(chave);
        DocumentReference userRef = usuariosRef.document(userApostaId.getUserId());
        Double resultadoConvertido = Double.parseDouble(resultado);

        Map<String, Object> updates = new HashMap<>();
        updates.put("estado", "fechado");
        updates.put("resultado", resultadoConvertido);

        Map<String, Object> updatesUser = new HashMap<>();

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);

                //Vai somar/subtrair o resultado da aposta com o saldo atual
                if (Double.parseDouble(UsuarioLogado.userLogado.getSaldo()) >= 0){
                    Double saldoAtt;
                    if (resultadoConvertido < 0){
                        saldoAtt = (Double.parseDouble(usuario.getSaldo()) +
                                resultadoConvertido);
                        updatesUser.put("saldo", saldoAtt.toString());
                    }else {
                        saldoAtt = (Double.parseDouble(usuario.getSaldo()) +
                                resultadoConvertido);
                        updatesUser.put("saldo", saldoAtt.toString());
                    }
                }

                //Vai atualizar o saldo do usuario
                userRef.update(updatesUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Sucesso ao atualizar o saldo do usuário", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Falha ao atualizar os dados do usuário: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Falha ao atualizar o saldo do usuario", Toast.LENGTH_SHORT).show();
                            }
                        });


        ref.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Aposta encerrada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Erro ao encerrar a aposta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dataInitialize(View view) {
        nomeTimeA = view.findViewById(R.id.textView_nomeTimeA);
        nomeTimeB = view.findViewById(R.id.textView_nomeTimeB);
        spinner = view.findViewById(R.id.spinner_apostas);
        todasAsApostas = view.findViewById(R.id.text_todas_as_apostasAdmin);
        valorApostado = view.findViewById(R.id.editText_valor_aposta);
        resultado = view.findViewById(R.id.editText_valor_apostaResultadoAdmin);
        botaoEncerrarAposta = view.findViewById(R.id.buttonEncerrarAposta);
        db = FirebaseFirestore.getInstance();
        apostasRef = db.collection("apostas");
        usuariosRef = db.collection("usuarios");
        apostaModelDbs = new ArrayList<>();

        apostasRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getContext(), "Erro ao inicializar os dados", Toast.LENGTH_SHORT).show();
                }

                if (value != null){
                    List<String> nomeApostas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value){
                        ApostaModelDb aposta = document.toObject(ApostaModelDb.class);

                        if (aposta.getEstado().equals("aberto")){
                            String nomeAposta = document.getId();
                            nomeApostas.add(nomeAposta);

                            apostaModelDbs.add(aposta);
                        }

                    }

                    if (getContext() != null){
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                                nomeApostas);
                        spinner.setAdapter(adapter);
                    }
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!apostaModelDbs.isEmpty()){
                    Picasso.with(getContext())
                            .load(apostaModelDbs.get(i).getLogoTimeaUrl())
                            .fit()
                            .centerCrop()
                            .into(nomeTimeA);

                    Picasso.with(getContext())
                            .load(apostaModelDbs.get(i).getLogoTimebUrl())
                            .fit()
                            .centerCrop()
                            .into(nomeTimeB);
                }

                todasAsApostas.setText(apostaModelDbs.get(i).getApostas());
                valorApostado.setText(apostaModelDbs.get(i).getValorAposta().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        botaoEncerrarAposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItem() != null){
                    encerrarAposta(spinner.getSelectedItem().toString(), resultado.getText().toString(),
                            apostaModelDbs.get(spinner.getSelectedItemPosition()));
                }
            }
        });

    }

}