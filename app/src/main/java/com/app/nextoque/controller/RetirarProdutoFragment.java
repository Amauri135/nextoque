package com.app.nextoque.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.nextoque.R;
import com.app.nextoque.databinding.FragmentRetirarProdutoBinding;
import com.app.nextoque.entity.Obra;
import com.app.nextoque.entity.Produto;
import com.app.nextoque.entity.Usuario;
import com.app.nextoque.model.ObraBO;
import com.app.nextoque.model.ProdutoBO;
import com.google.android.material.navigation.NavigationView;

public class RetirarProdutoFragment extends Fragment {
    private FragmentRetirarProdutoBinding binding;
    private final NavigationView navigationView;
    private final Usuario usuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRetirarProdutoBinding.inflate(inflater, container, false);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);

        titulo.setText("RETIRAR PRODUTO");

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.formularioRetiradaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.prodRetirarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.obraRetirarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.quantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.observacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        new ProdutoBO(getContext(), usuario, getActivity().getSupportFragmentManager()).buscarProdutosRetirarProduto(binding.spinnerProdutos);

        new ObraBO(getContext(), usuario, getActivity().getSupportFragmentManager()).buscarObrasRetirarProduto(binding.spinnerObras);

        binding.salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantidade =
                        binding.quantidade.getEditText() == null || binding.quantidade.getEditText().getText().toString().trim().isEmpty() ?
                                null :
                                Integer.parseInt(binding.quantidade.getEditText().getText().toString());

                String obs =
                        binding.observacao.getEditText() != null ?
                                binding.observacao.getEditText().getText().toString() :
                                null;

                new ProdutoBO(getContext(), usuario, getActivity().getSupportFragmentManager()).retirarProduto(
                        (Produto) binding.spinnerProdutos.getSelectedItem(),
                        quantidade,
                        (Obra) binding.spinnerObras.getSelectedItem(),
                        obs
                );
            }
        });

        return binding.getRoot();
    }

    private void esconderNavigationView() {
        if(navigationView.getVisibility() == View.VISIBLE){
            navigationView.setVisibility(View.GONE);
        }
    }

    public RetirarProdutoFragment(NavigationView navigationView, Usuario usuario) {
        this.navigationView = navigationView;
        this.usuario = usuario;
    }
}
