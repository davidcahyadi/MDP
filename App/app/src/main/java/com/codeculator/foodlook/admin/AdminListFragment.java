package com.codeculator.foodlook.admin;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.admin.AdminRecipeListAdapter;
import com.codeculator.foodlook.adapter.admin.AdminUserListAdapter;
import com.codeculator.foodlook.home.ActivityHome;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    AdminRecipeListAdapter recipesAdapter;
    AdminUserListAdapter usersAdapter;

    ArrayList<Recipe> recipes;
    ArrayList<User> users;

    RecyclerView listRV;
    String type;

    LottieAnimationView loading;

    String selectedType;
    int selectedIndex;

    ItemTouchHelper.SimpleCallback simpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            switch(direction){
                case ItemTouchHelper.LEFT:
                    //TODO: implement delete
                    Toast.makeText(getActivity(), "Deleting", Toast.LENGTH_SHORT).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    //TODO: implement detail
                    Toast.makeText(getActivity(), "Displaying detail", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_500))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_500))
                    .addSwipeRightActionIcon(R.drawable.ic_info_white_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive);
        }
   };

    public AdminListFragment() {
        // Required empty public constructor
    }

    public static AdminListFragment newInstance(String param1, String param2) {
        AdminListFragment fragment = new AdminListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            type = b.getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listRV = view.findViewById(R.id.listRV);
        listRV.setLayoutManager(new LinearLayoutManager(getContext()));
        setHasOptionsMenu(true);
        loading = view.findViewById(R.id.loading_list_spinner);

        if(type.equalsIgnoreCase("recipes"))
            loadAllRecipes();
        else if(type.equalsIgnoreCase("users"))
            loadAllUsers();
        else if(type.equalsIgnoreCase("reviews"))
            loadAllReviews();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(listRV);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.admin_toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_keyword);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(type.equalsIgnoreCase("users")){
                    ArrayList<User> searchedUsers = new ArrayList<>();
                    if(users != null){
                        for(User u : users){
                            if(s.equalsIgnoreCase("") ||
                                    u.getName().toLowerCase().contains(s.toLowerCase()))
                                searchedUsers.add(u);
                        }
                        usersAdapter = new AdminUserListAdapter(searchedUsers, getContext());
                        setUsersAdapterInterface();
                        listRV.setAdapter(usersAdapter);
                    }
                }else if(type.equalsIgnoreCase("recipes")){
                    ArrayList<Recipe> searchedRecipes = new ArrayList<>();
                    if(recipes != null){
                        for(Recipe r : recipes){
                            if(s.equalsIgnoreCase("") ||
                                    r.title.toLowerCase().contains(s.toLowerCase()))
                                searchedRecipes.add(r);
                        }
                        recipesAdapter = new AdminRecipeListAdapter(searchedRecipes, getContext());
                        setRecipesAdapterInterface();
                        listRV.setAdapter(recipesAdapter);
                    }

                }else{
                    //TODO: implement reviews
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadAllRecipes(){
        Call<ArrayList<Recipe>> call = RetrofitApi.getInstance().getAdminService().getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    recipes = new ArrayList<>();
                    recipes.addAll(response.body());
                    recipesAdapter = new AdminRecipeListAdapter(response.body(), getContext());
                    setRecipesAdapterInterface();
                    listRV.setAdapter(recipesAdapter);
                    loading.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });
    }

    public void loadAllUsers(){
        Call<ArrayList<User>> call = RetrofitApi.getInstance().getAdminService().getUsers();
        call.enqueue((new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.isSuccessful()){
                    users = new ArrayList<>();
                    users.addAll(response.body());
                    usersAdapter = new AdminUserListAdapter(users, getContext());
                    setUsersAdapterInterface();
                    listRV.setAdapter(usersAdapter);
                    loading.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        }));
    }

    public void loadAllReviews(){
        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getAdminService().getReviews();
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    //TODO: implement reviewsAdapter
                    loading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }

    public void setRecipesAdapterInterface(){
        recipesAdapter.setListClickListener(new AdminUserListAdapter.ListClickListener() {
            @Override
            public void moreButtonClick(int position, ImageButton btn) {
                showPopup(btn);
                selectedType = "recipes";
                selectedIndex = position;
            }
        });
    }

    public void setUsersAdapterInterface(){
        usersAdapter.setListClickListener(new AdminUserListAdapter.ListClickListener() {
            @Override
            public void moreButtonClick(int position, ImageButton btn) {
                showPopup(btn);
                selectedType = "users";
                selectedIndex = position;
            }
        });
    }

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.admin_list_item_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item_detail:
                showRecipeDetail();
                return true;
            case R.id.item_delete:
                showDeleteConfirmation();
                return true;
            default:
                return false;
        }
    }

    public void showRecipeDetail(){

    }

    public void showDeleteConfirmation(){

    }
}