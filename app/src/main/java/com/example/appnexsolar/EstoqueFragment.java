package com.example.appnexsolar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnexsolar.databinding.FragmentEstoqueBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EstoqueFragment extends Fragment {

    private FragmentEstoqueBinding binding;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView viewMenuAcoes;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentEstoqueBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerViewEstoque;
        progressBar = binding.progressBar;
        viewMenuAcoes = binding.ViewMenuAcoes;

        progressBar.setVisibility(View.VISIBLE);

        final DatabaseReference usuariosReference = FirebaseDatabase.getInstance().getReference("usuarios");

        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(getContext(), "Não há usuário logado!", Toast.LENGTH_LONG).show();
            requireActivity().getSupportFragmentManager().getFragments().clear();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }

        List<Produto> produtos = new ArrayList<>();
        ProdutoAdapter produtoAdapter = new ProdutoAdapter(getContext(), produtos, usuario, viewMenuAcoes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(produtoAdapter);

        usuariosReference.child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        usuario = task.getResult().getValue(Usuario.class);
                        produtoAdapter.setUsuario(usuario);
                        if (usuario != null) {
                            carregaItens(produtos, produtoAdapter);
                        }
                    }
                }
            }
        });

        return binding.getRoot();
    }

    public void carregaItens(List<Produto> produtos, ProdutoAdapter produtoAdapter) {

        DatabaseReference estoqueReference = FirebaseDatabase.getInstance().getReference("filiais/-MlrWiX0mZJogkqzQJZ5/estoque/produtos");

        estoqueReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChildren()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        Produto produto = child.getValue(Produto.class);

                        if(produto != null){
                            produto.setId(child.getKey());
                            produtos.add(produto);
                            produtoAdapter.notifyItemInserted(produtos.size() - 1);
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}