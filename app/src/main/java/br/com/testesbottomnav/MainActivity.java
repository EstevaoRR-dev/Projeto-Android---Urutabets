package br.com.testesbottomnav;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.com.testesbottomnav.databinding.ActivityMainBinding;
import br.com.testesbottomnav.util.UsuarioLogado;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    private ActivityMainBinding binding;

    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initNavigation();
    }

    private void initNavigation(){

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    String nome, sobrenome, email, dataNasc, telefone, userType, saldo;

                    UsuarioLogado usuarioLogado = new UsuarioLogado();

                    nome = documentSnapshot.getString("nome");
                    email = documentSnapshot.getString("email");
                    dataNasc = documentSnapshot.getString("dataNasc");
                    sobrenome = documentSnapshot.getString("sobrenome");
                    telefone = documentSnapshot.getString("telefone");
                    userType = documentSnapshot.getString("userType");
                    saldo = documentSnapshot.getString("saldo");

                    UsuarioLogado.setUserLogado(nome,email, telefone, dataNasc, sobrenome, userType, saldo);

                    if (!userType.equals("admin")){
                        Menu menu = binding.bottomNav.getMenu();
                        menu.removeItem(R.id.adminArea);
                    }

                }
            }
        });
    }
}