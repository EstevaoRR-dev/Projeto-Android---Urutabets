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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.testesbottomnav.ItemNoticia;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.ClubeModelDb;
import br.com.testesbottomnav.util.CodificaDecodificaUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminNoticias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminNoticias extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imageUpload;
    private TextInputEditText titulo;
    private TextInputEditText descricao;
    private TextInputEditText linkNoticia;
    private ImageView imageUpload2;
    private TextInputEditText titulo2;
    private TextInputEditText descricao2;
    private TextInputEditText linkNoticia2;
    private Spinner spinnerNomes;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Button botaoUploadDispositivo, botaoUploadDb, botaoSalvarAlteracoes, botaoRemoverNoticia;
    private DatabaseReference mDataBaseRef;
    private Uri mImageUri;
    private String url = "";
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private List<String> titulosNoticias;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminNoticias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminNoticias.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminNoticias newInstance(String param1, String param2) {
        AdminNoticias fragment = new AdminNoticias();
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
        return inflater.inflate(R.layout.fragment_admin_noticias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize(view);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == -1 && result.getData() != null){
                        mImageUri = result.getData().getData();

                        Picasso.with(view.getContext()).load(mImageUri).into(imageUpload);
                    }
                });
    }



    private void openFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(i);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
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
                                            Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                                            ItemNoticia itemNoticia = new ItemNoticia(url, titulo.getText().toString(),
                                                    descricao.getText().toString(), linkNoticia.getText().toString());

                                            mDataBaseRef.child(itemNoticia.getTituloNoticia()).setValue(itemNoticia);
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

    //Ação de remover uma noticia
    private void removerNoticia(String chave) {
        DatabaseReference noticiaRef = mDataBaseRef.child(chave);

        noticiaRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Não foi possivel remover: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Ação de salvar alterações
    private void salvarAlteracoes(String chave, Map<String, Object> noticia) {

        DatabaseReference noticiaRef = mDataBaseRef.child(chave);

        noticiaRef.updateChildren(noticia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Não foi possivel atualizar a noticia: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dataInitialize(View view){
        imageUpload = view.findViewById(R.id.imageUpload_noticia);
        titulo = view.findViewById(R.id.textEdit_tituloNoticia);
        descricao = view.findViewById(R.id.textEdit_descricaoNoticia);
        linkNoticia = view.findViewById(R.id.textEdit_urlNoticia);
        imageUpload2 = view.findViewById(R.id.imageNoticia);
        titulo2 = view.findViewById(R.id.textEdit_tituloNoticiaAddUp);
        descricao2 = view.findViewById(R.id.textEdit_descricaoNoticiaAddUp);
        linkNoticia2 = view.findViewById(R.id.textEdit_urlNoticiaUppAdd);
        spinnerNomes = view.findViewById(R.id.spinner_nomeNoticias);
        botaoUploadDispositivo = view.findViewById(R.id.botaoUploadNoticia);
        botaoUploadDb = view.findViewById(R.id.botaoUpload2Noticia);
        botaoSalvarAlteracoes = view.findViewById(R.id.botaoAtualizarNoticia);
        botaoRemoverNoticia = view.findViewById(R.id.botaoRemoverNoticia);
        mStorageRef = FirebaseStorage.getInstance().getReference("noticias");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("noticias");
        titulosNoticias = new ArrayList<>();

        botaoUploadDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        botaoUploadDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });

        //Vai preencher o spiner com os titulos das noticias
        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titulosNoticias.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    titulosNoticias.add(childSnapshot.getKey());
                }
                if (getContext() != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                            titulosNoticias);
                    spinnerNomes.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Não foi possivel recuperar os nomes", Toast.LENGTH_SHORT).show();
            }
        });

        //Preencher os campos com os dados do spinner
        spinnerNomes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference noticiaRef = mDataBaseRef.child(spinnerNomes.getSelectedItem().toString());

                noticiaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ItemNoticia noticia = snapshot.getValue(ItemNoticia.class);

                        Picasso.with(getContext())
                                .load(noticia.getImagemNoticia())
                                .fit()
                                .centerCrop()
                                .into(imageUpload2);

                        titulo2.setText(noticia.getTituloNoticia());
                        descricao2.setText(noticia.getCorpoNoticia());
                        linkNoticia2.setText(noticia.getUrl());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Não foi possivel recuperar a noticia", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        botaoRemoverNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerNoticia(spinnerNomes.getSelectedItem().toString());
            }
        });

        botaoSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> noticia = new HashMap<>();
                noticia.put("tituloNoticia", titulo2.getText().toString());
                noticia.put("corpoNoticia", descricao2.getText().toString());
                noticia.put("url", linkNoticia2.getText().toString());
                salvarAlteracoes(spinnerNomes.getSelectedItem().toString(), noticia);
            }
        });

    }

}