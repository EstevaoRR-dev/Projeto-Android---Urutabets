package br.com.testesbottomnav;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import br.com.testesbottomnav.fragments.ContaFrag;
import br.com.testesbottomnav.fragments.HistoricoApostasFrag;
import br.com.testesbottomnav.fragments.MinhasApostasFrag;

public class FragmentsAdapterApostas extends FragmentStateAdapter {

    public FragmentsAdapterApostas(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0){
            return new ContaFrag();
        } else if (position == 1) {
            return new MinhasApostasFrag();
        }

        return new HistoricoApostasFrag();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
