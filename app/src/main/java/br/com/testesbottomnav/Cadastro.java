package br.com.testesbottomnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import br.com.testesbottomnav.model.Usuario;
import br.com.testesbottomnav.util.ConfiguracaoBd;

public class Cadastro extends AppCompatActivity {

    Usuario usuario;
    FirebaseAuth autenticacao;
    EditText nome, email, senha, telefone, dataNasc, sobrenome;
    Button botao;

    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializar();
    }

    public void validarCampos(View view){
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

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoBd.firebaseAutencicacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Cadastro.this, "Sucesso ao Cadastrar", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(Cadastro.this, execao, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        usuarios.put("userType", "comum");
        usuarios.put("saldo", "0.00");

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

    private void inicializar(){
        nome = findViewById(R.id.nomeCadastro);
        email = findViewById(R.id.emailCadastro);
        senha = findViewById(R.id.senhaCadastro);
        telefone = findViewById(R.id.telefoneCadastro);
        dataNasc = findViewById(R.id.data_nascCadastro);
        sobrenome = findViewById(R.id.sobrenomeCadastro);
        botao = findViewById(R.id.botaoCadastrarCadastro);
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
                                Toast.makeText(this, "Preencha a data de nascimento", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "Preencha o telefone", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Preencha o sobrenome", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show();
        }
        return  false;
    }
}