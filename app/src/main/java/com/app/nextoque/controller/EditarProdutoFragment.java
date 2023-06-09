package com.app.nextoque.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.nextoque.R;
import com.app.nextoque.databinding.FragmentEditarProdutoBinding;
import com.app.nextoque.entity.Produto;
import com.app.nextoque.entity.Usuario;
import com.app.nextoque.model.ProdutoBO;
import com.google.android.material.navigation.NavigationView;

public class EditarProdutoFragment extends Fragment {
    private FragmentEditarProdutoBinding binding;
    private final Usuario usuario;
    private Produto produto;
    private final NavigationView navigationView;

    public EditarProdutoFragment(Usuario usuario, Produto produto, NavigationView navigationView) {
        this.usuario = usuario;
        this.produto = produto;
        this.navigationView = navigationView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditarProdutoBinding.inflate(inflater, container, false);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);

        titulo.setText("EDITAR PRODUTO");

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.formularioEditarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.descricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.categoria.setOnClickListener(new View.OnClickListener() {
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

        binding.unidadeMedida.setOnClickListener(new View.OnClickListener() {
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

        binding.observacaoAlteracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.descricao.getEditText().setText(produto.getDescricao());
        binding.categoria.getEditText().setText(produto.getCategoria());
        binding.quantidade.getEditText().setText(produto.getQuantidadeInicial().toString());
        binding.unidadeMedida.getEditText().setText(produto.getUnidadeMedida());
        binding.observacao.getEditText().setText(produto.getObs());

        binding.salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descricao = binding.descricao.getEditText().getText() != null
                        && !binding.descricao.getEditText().getText().toString().trim().isEmpty() ?
                        binding.descricao.getEditText().getText().toString() :
                        null;

                String categoria = binding.categoria.getEditText().getText() != null
                        && !binding.categoria.getEditText().getText().toString().trim().isEmpty()?
                        binding.categoria.getEditText().getText().toString() :
                        null;

                Long quantidade = binding.quantidade.getEditText().getText() != null
                        && !binding.quantidade.getEditText().getText().toString().trim().isEmpty()?
                        Long.parseLong(binding.quantidade.getEditText().getText().toString()) :
                        null;

                String unidadeMedida = binding.unidadeMedida.getEditText().getText() != null
                        && !binding.unidadeMedida.getEditText().getText().toString().trim().isEmpty()?
                        binding.unidadeMedida.getEditText().getText().toString() :
                        null;

                String obs = binding.observacao.getEditText().getText() != null ?
                        binding.observacao.getEditText().getText().toString() :
                        null;

                String obsAlteracao = binding.observacaoAlteracao.getEditText().getText() != null ?
                        binding.observacaoAlteracao.getEditText().getText().toString()
                        : null;

                if(descricao == null || descricao.trim().isEmpty()
                        || categoria == null ||  categoria.trim().isEmpty()
                        || quantidade == null || unidadeMedida == null) {
                    Toast.makeText(getContext(), "Informe todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
                } else if(quantidade == 0L) {
                    Toast.makeText(getContext(), "A quantidade não pode ser zero!", Toast.LENGTH_SHORT).show();
                } else {
                        produto.setDescricao(descricao);
                        produto.setCategoria(categoria);
                        produto.setQuantidadeInicial(quantidade);
                        produto.setUnidadeMedida(unidadeMedida);
                        produto.setObs(obs);

                        new ProdutoBO(getContext(), usuario, getActivity().getSupportFragmentManager()).editarProduto(produto, obsAlteracao);
                }
            }
        });

        return binding.getRoot();
    }

    private void esconderNavigationView() {
        if(navigationView.getVisibility() == View.VISIBLE) {
            navigationView.setVisibility(View.GONE);
        }
    }
}
