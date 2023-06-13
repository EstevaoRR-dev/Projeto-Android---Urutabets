package br.com.testesbottomnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.testesbottomnav.model.Usuario;
import br.com.testesbottomnav.util.ConfiguracaoBd;

public class Login extends AppCompatActivity {

    EditText email, senha;
    Button botaoAcessar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = ConfiguracaoBd.firebaseAutencicacao();
        inicializarComponentes();

    }

    public void validarAutenticacao(View view){
        String emailUser = email.getText().toString();
        String senhaUser = senha.getText().toString();

        if (!emailUser.isEmpty()){
            if (!senhaUser.isEmpty()){

                Usuario usuario = new Usuario();

                usuario.setEmail(emailUser);
                usuario.setSenha(senhaUser);

                logar(usuario);

            }else {
                Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT).show();
        }

    }

    private void logar(Usuario usuario) {

        auth.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    abrirHome();
                }else {

                    String execao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        execao = "Usuário não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        execao = "Email ou Senha incorretos";
                    }catch (Exception e){
                        execao = "Erro ao logar" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, execao, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void abrirHome(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void fazerCadastro(View view){
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }

    private void inicializarComponentes(){
        email = findViewById(R.id.emailLogin);
        senha = findViewById(R.id.senhaLogin);
        botaoAcessar = findViewById(R.id.botaoAcessar);
    }

}