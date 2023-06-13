package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.testesbottomnav.Cadastro;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.Usuario;
import br.com.testesbottomnav.util.ConfiguracaoBd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCadastro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCadastro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Usuario usuario;
    FirebaseAuth autenticacao;
    EditText nome, email, senha, telefone, dataNasc, sobrenome;
    Button botao;

    String usuarioId;

    public AdminCadastro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminCadastro.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminCadastro newInstance(String param1, String param2) {
        AdminCadastro fragment = new AdminCadastro();
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
        return inflater.inflate(R.layout.fragment_admin_cadastro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializar(view);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });
    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoBd.firebaseAutencicacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Sucesso ao cadastrar", Toast.LENGTH_SHORT).show();
                    salvarDadosUser();
                }else{
                    String execao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        execao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        execao = "email invalido";
                    }catch (FirebaseAuthUserCollisionException e){
                        execao = "Esta conta já existe";
                    }catch (Exception e){
                        execao = "Erro ao Cadastrar Usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), execao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarCampos(){
        String nomeUser = nome.getText().toString();
        String emailUser = email.getText().toString();
        String senhaUser = senha.getText().toString();
        String telefoneUser = telefone.getText().toString();
        String dataNascUser = dataNasc.getText().toString();
        String sobrenomeUser = sobrenome.getText().toString();

        if(validacoes(nomeUser,emailUser, senhaUser, telefoneUser, dataNascUser, sobrenomeUser)){
            usuario = new Usuario();

            usuario.setNome(nomeUser);
            usuario.setEmail(emailUser);
            usuario.setSenha(senhaUser);
            //cadastro
            cadastrarUsuario();
        }
    }

    private void salvarDadosUser(){
        String nomeUser = nome.getText().toString();
        String emailUser = email.getText().toString();
        String telefoneUser = telefone.getText().toString();
        String dataNascUser = dataNasc.getText().toString();
        String sobrenomeUser = sobrenome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();

        usuarios.put("nome", nomeUser);
        usuarios.put("email", emailUser);
        usuarios.put("telefone", telefoneUser);
        usuarios.put("dataNasc", dataNascUser);
        usuarios.put("sobrenome", sobrenomeUser);
        usuarios.put("userType", "admin");
        usuarios.put("saldo", "0,00");

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados" + e.toString());
            }
        });

    }

    private void inicializar(View view){
        nome = view.findViewById(R.id.nomeCadastroAdmin);
        email = view.findViewById(R.id.emailCadastroAdmin);
        senha = view.findViewById(R.id.senhaCadastroAdmin);
        telefone = view.findViewById(R.id.telefoneCadastroAdmin);
        dataNasc = view.findViewById(R.id.data_nascCadastroAdmin);
        sobrenome = view.findViewById(R.id.sobrenomeCadastroAdmin);
        botao = view.findViewById(R.id.botaoCadastrarCadastroAdmin);
    }

    private boolean validacoes(String nomeUser, String emailUser, String senhaUser, String telefoneUser,
                               String dataNascUser, String sobrenomeUser) {
        if (!nomeUser.isEmpty()){
            if (!emailUser.isEmpty()){
                if (!senhaUser.isEmpty()){
                    if (!sobrenomeUser.isEmpty()){
                        if (!telefoneUser.isEmpty()){
                            if (!dataNascUser.isEmpty()){
                                return true;
                            }
                            else {
                                Toast.makeText(getContext(), "Preencha a data de nascimento", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Preencha o telefone", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Preencha o sobrenome", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Preencha a senha", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getContext(), "Preencha o email", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getContext(), "Preencha o nome", Toast.LENGTH_SHORT).show();
        }
        return  false;
    }
}