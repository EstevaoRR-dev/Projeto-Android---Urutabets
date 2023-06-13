package br.com.testesbottomnav;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import br.com.testesbottomnav.fragments.BasquetFrag;
import br.com.testesbottomnav.fragments.FutebolFrag;

public class FragmentsAdapterJogos extends FragmentStateAdapter {

    public FragmentsAdapterJogos(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new BasquetFrag();
        }

        return new FutebolFrag();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
