package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import br.com.testesbottomnav.R;
import br.com.testesbottomnav.util.UsuarioLogado;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContaFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContaFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextInputEditText contaNome, contaEmail, contaDataNasc, contaSobrenome, contaTelefone;

    public ContaFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContaFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ContaFrag newInstance(String param1, String param2) {
        ContaFrag fragment = new ContaFrag();
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
        return inflater.inflate(R.layout.fragment_conta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize(view);
    }

    private void dataInitialize(View view){
        contaNome = view.findViewById(R.id.conta_nomeUser);
        contaEmail = view.findViewById(R.id.conta_emailUser);
        contaSobrenome = view.findViewById(R.id.conta_sobrenome);
        contaTelefone = view.findViewById(R.id.conta_telefone);
        contaDataNasc = view.findViewById(R.id.conta_dataNasc);

        contaNome.setText(UsuarioLogado.userLogado.getNome());
        contaEmail.setText(UsuarioLogado.userLogado.getEmail());
        contaSobrenome.setText(UsuarioLogado.userLogado.getSobrenome());
        contaTelefone.setText(UsuarioLogado.userLogado.getTelefone());
        contaDataNasc.setText(UsuarioLogado.userLogado.getDatanasc());
    }
}