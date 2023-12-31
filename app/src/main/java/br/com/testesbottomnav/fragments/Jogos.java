package br.com.testesbottomnav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import br.com.testesbottomnav.Adapter;
import br.com.testesbottomnav.FragmentsAdapterJogos;
import br.com.testesbottomnav.Item;
import br.com.testesbottomnav.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Jogos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jogos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Item> items;
    private RecyclerView recyclerView;
    private FragmentsAdapterJogos fragmentsAdapterJogos;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public Jogos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jogos.
     */
    // TODO: Rename and change types and number of parameters
    public static Jogos newInstance(String param1, String param2) {
        Jogos fragment = new Jogos();
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
        return inflater.inflate(R.layout.fragment_jogos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab_Layout);
        viewPager2 = view.findViewById(R.id.pager2_jogos);

        tabLayout.addTab(tabLayout.newTab().setText("Futebol"));
        tabLayout.addTab(tabLayout.newTab().setText("Basquete"));

        FragmentManager fragmentManager = getChildFragmentManager();

        fragmentsAdapterJogos = new FragmentsAdapterJogos(fragmentManager, getLifecycle());

        viewPager2.setAdapter(fragmentsAdapterJogos);

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

}