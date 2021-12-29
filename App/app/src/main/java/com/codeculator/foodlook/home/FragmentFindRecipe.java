package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.FragmentCatalogBinding;
import com.codeculator.foodlook.databinding.FragmentFindRecipeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFindRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFindRecipe extends Fragment {

    FragmentFindRecipeBinding binding;


    public FragmentFindRecipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFindRecipe.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFindRecipe newInstance(String param1, String param2) {
        FragmentFindRecipe fragment = new FragmentFindRecipe();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindRecipeBinding.inflate(inflater,container, false);
        Log.i("CREATE","VIEW");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}