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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import br.com.testesbottomnav.FragmentsAdapterAdmimTabs;
import br.com.testesbottomnav.R;
import br.com.testesbottomnav.model.ClubeModelDb;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPage extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    //Atributos oficiais
    private ViewPager2 pager2;
    private TabLayout tabLayout;
    private FragmentsAdapterAdmimTabs admimTabs;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminPage.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminPage newInstance(String param1, String param2) {
        AdminPage fragment = new AdminPage();
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
        return inflater.inflate(R.layout.fragment_admin_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager2 = view.findViewById(R.id.pager2_admin);
        tabLayout = view.findViewById(R.id.tab_Layout_admin_control);

        tabLayout.addTab(tabLayout.newTab().setText("Jogos"));
        tabLayout.addTab(tabLayout.newTab().setText("Notícias"));
        tabLayout.addTab(tabLayout.newTab().setText("Cadastro"));
        tabLayout.addTab(tabLayout.newTab().setText("Apostas"));

        FragmentManager fragmentManager = getChildFragmentManager();

        admimTabs = new FragmentsAdapterAdmimTabs(fragmentManager, getLifecycle());

        pager2.setAdapter(admimTabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}