package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityHomeBinding;
import com.codeculator.foodlook.databinding.FragmentCatalogBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalog extends Fragment {

    FragmentCatalogBinding binding;

    public FragmentCatalog() {
        // Required empty public constructor
    }

    public static FragmentCatalog newInstance() {
        FragmentCatalog fragment = new FragmentCatalog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatalogBinding.inflate(inflater,container, false);
        Log.i("CREATE","VIEW");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnTest.setOnClickListener(v->{
            FragmentManager fm = getParentFragmentManager();
            Fragment f = new FragmentRecipeDetail();
            Bundle b = new Bundle();
            b.putInt("ID",1);
            f.setArguments(b);
            fm.beginTransaction().replace(R.id.container,f).commit();
        });
    }
}