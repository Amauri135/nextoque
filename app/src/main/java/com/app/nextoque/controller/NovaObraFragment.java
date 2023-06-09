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
import com.app.nextoque.databinding.FragmentNovaObraBinding;
import com.app.nextoque.entity.Usuario;
import com.app.nextoque.model.ObraBO;
import com.google.android.material.navigation.NavigationView;

public class NovaObraFragment extends Fragment {
    private FragmentNovaObraBinding binding;
    private final Usuario usuario;
    private final NavigationView navigationView;

    public NovaObraFragment(Usuario usuario, NavigationView navigationView) {
        this.usuario = usuario;
        this.navigationView = navigationView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNovaObraBinding.inflate(inflater, container, false);

        TextView titulo = (TextView) getActivity().findViewById(R.id.titulo);

        titulo.setText("NOVA OBRA");

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.formularioCadastroObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.cidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.responsavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderNavigationView();
            }
        });

        binding.salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = (binding.nome.getEditText().getText() == null
                        || binding.nome.getEditText().getText().toString().trim().isEmpty()) ?
                        null
                        : binding.nome.getEditText().getText().toString();

                String cidade = (binding.cidade.getEditText().getText() == null
                        || binding.cidade.getEditText().getText().toString().trim().isEmpty()) ?
                        null
                        : binding.cidade.getEditText().getText().toString();

                String endereco = (binding.endereco.getEditText().getText() == null
                        || binding.endereco.getEditText().getText().toString().trim().isEmpty()) ?
                        null
                        : binding.nome.getEditText().getText().toString();

                String responsavel = (binding.responsavel.getEditText().getText() == null
                        || binding.responsavel.getEditText().getText().toString().trim().isEmpty()) ?
                        null
                        : binding.responsavel.getEditText().getText().toString();

                new ObraBO(getContext(), usuario, getActivity().getSupportFragmentManager()).salvarObra(nome, endereco, cidade, responsavel, navigationView);
            }
        });

        return binding.getRoot();
    }

    private void esconderNavigationView() {
        if (navigationView.getVisibility() == View.VISIBLE) {
            navigationView.setVisibility(View.GONE);
        }
    }
}
