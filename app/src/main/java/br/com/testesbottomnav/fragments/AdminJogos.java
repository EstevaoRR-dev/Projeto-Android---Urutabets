package br.com.testesbottomnav.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.ClubeModelDb;
import br.com.testesbottomnav.model.PartidaModelDb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminJogos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminJogos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button buttonUpload, buttonUpload2, buttonCadastrarPartida, buttonRemoverPartida;
    private ImageView image, imageTimeaAdmin, imageTimebAdmin;
    private Spinner spinnerNomeTimeA, spinnerNomeTimeB, spinnerLiga;
    private EditText data, hora;
    private Uri mImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private DatabaseReference pDataBaseRef;
    private Spinner spinnerPartidas;
    private String url = "";
    private StorageTask mUploadTask;
    private TextInputEditText nome, posicao;
    private List<ClubeModelDb> clubes;
    private List<String> nomeClubes;
    private List<String> nomePartidas;

    public AdminJogos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminJogos.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminJogos newInstance(String param1, String param2) {
        AdminJogos fragment = new AdminJogos();
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
        return inflater.inflate(R.layout.fragment_admin_jogos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize(view);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == -1 && result.getData() != null){
                        mImageUri = result.getData().getData();

                        Picasso.with(view.getContext()).load(mImageUri).into(image);
                    }
                });

    }

    public void openFileChooser(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(i);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Upload realizado com sucesso", Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            url = uri.toString();
                                            ClubeModelDb modelDb = new ClubeModelDb(nome.getText().toString(), url,
                                                    Integer.parseInt(posicao.getText().toString()));
                                            String uploadId = mDataBaseRef.push().getKey();
                                            mDataBaseRef.child(uploadId).setValue(modelDb);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Erro ao obter o URL da imagem", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        }
                    });
        }else {
            Toast.makeText(getContext(), "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarPartida(){
        try {
            PartidaModelDb partida = new PartidaModelDb(clubes.get(spinnerNomeTimeA.getSelectedItemPosition()).getImageUrl(),
                    clubes.get(spinnerNomeTimeB.getSelectedItemPosition()).getImageUrl(), spinnerNomeTimeA.getSelectedItem().toString(),
                    spinnerNomeTimeB.getSelectedItem().toString(), hora.getText().toString(), data.getText().toString(),
                    spinnerLiga.getSelectedItem().toString());

            String key = partida.getNomeTimeA() + " x " + partida.getNomeTimeB();

            pDataBaseRef.child(key).setValue(partida).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Sucessso ao cadastrar a partida", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Falha ao cadastrar a partida:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //Metodo que vai remover a partida do banco
    private void removerPartida(String chave) {
        DatabaseReference refPartida = pDataBaseRef.child(chave);

        refPartida.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Não foi possivel remover: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    public List<String> listaSpinner(@NonNull List<ClubeModelDb> clubes){
        List<String> nomesClubes = new ArrayList<>();

        for (ClubeModelDb clube : clubes){
            nomesClubes.add(clube.getNome());
        }
        return nomesClubes;
    }

    private void dataInitialize(View view){
        buttonUpload = view.findViewById(R.id.botaoUpload);
        buttonUpload2 = view.findViewById(R.id.botaoUpload2);
        buttonCadastrarPartida = view.findViewById(R.id.button_cadastrar_partida);
        nome = view.findViewById(R.id.textEdit_nomeClube);
        posicao = view.findViewById(R.id.textEdit_posicao);
        image = view.findViewById(R.id.imageUpload_clube);
        imageTimeaAdmin = view.findViewById(R.id.image_time_a_admin);
        imageTimebAdmin = view.findViewById(R.id.image_time_b_admin);
        spinnerNomeTimeA = view.findViewById(R.id.spinner_timeA_admin);
        spinnerNomeTimeB = view.findViewById(R.id.spinner_timeB_admin);
        spinnerLiga = view.findViewById(R.id.spinner_liga_admin);
        spinnerPartidas = view.findViewById(R.id.spinnerNomePartidas);
        data = view.findViewById(R.id.editText_data_admin);
        hora = view.findViewById(R.id.editText_hora_admin);
        clubes = new ArrayList<>();
        nomeClubes = new ArrayList<>();
        nomePartidas = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        pDataBaseRef = FirebaseDatabase.getInstance().getReference("partidas");
        buttonRemoverPartida = view.findViewById(R.id.button_remover_partida);

        //Ação do botão de selecionar imagem do dispositivo
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //Ação de fazer upload no banco
        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });

        //Ação de cadastrar uma nova partida
        buttonCadastrarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarPartida();
            }
        });

        //Ouvinte que vai preencher os spinner de nome dos clubes,
        // toda vez que o banco for modificado
        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clubes.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    ClubeModelDb  modelDb = postSnapshot.getValue(ClubeModelDb.class);
                    clubes.add(modelDb);
                }

                nomeClubes = listaSpinner(clubes);

                if(getContext() != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
                            , android.R.layout.simple_spinner_dropdown_item, nomeClubes);

                    spinnerNomeTimeA.setAdapter(adapter);
                    spinnerNomeTimeB.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Bloco vai preencher os imageView de acordo com o nome dos spinner
        try {
            spinnerNomeTimeA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Picasso.with(getContext())
                            .load(clubes.get(i).getImageUrl())
                            .fit()
                            .centerCrop()
                            .into(imageTimeaAdmin);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spinnerNomeTimeB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Picasso.with(getContext())
                            .load(clubes.get(i).getImageUrl())
                            .fit()
                            .centerCrop()
                            .into(imageTimebAdmin);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //Recuperando o nome das partidas
        pDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nomePartidas.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    nomePartidas.add(childSnapshot.getKey());
                }
                if (getContext() != null){
                    ArrayAdapter<String> adapterPartidasNome = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                            nomePartidas);
                    spinnerPartidas.setAdapter(adapterPartidasNome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao buscar os nomes das partidas", Toast.LENGTH_SHORT).show();
            }
        });


        //Ação de remover uma partida
        buttonRemoverPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerPartida(spinnerPartidas.getSelectedItem().toString());
            }
        });

    }


}