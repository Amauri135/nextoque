package com.app.nextoque.model;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nextoque.R;
import com.app.nextoque.adapter.ListarProdutosAdapter;
import com.app.nextoque.controller.ListarProdutosFragment;
import com.app.nextoque.controller.NovoProdutoFragment;
import com.app.nextoque.entity.Acao;
import com.app.nextoque.entity.Obra;
import com.app.nextoque.entity.Produto;
import com.app.nextoque.entity.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProdutoBO {

    private final Context context;
    private DatabaseReference produtosReference;
    private final FragmentManager fragmentManager;
    private final Usuario usuario;

    public ProdutoBO(Context context, Usuario usuario, FragmentManager fragmentManager) {
        this.context = context;
        this.usuario = usuario;
        this.produtosReference = FirebaseDatabase.getInstance().getReference("filiais/" + usuario.getIdFilial() + "/estoque/produtos/");
        this.fragmentManager = fragmentManager;
    }

    public void retirarProduto(Produto produto, Integer quantidade, Obra obra, String obs) {
        if(produto == null || "selecione".equals(produto.getId())) {
            Toast.makeText(context, "Selecione um produto!", Toast.LENGTH_SHORT).show();
        } else if(obra ==  null || "selecione".equals(obra.getId())) {
            Toast.makeText(context, "Selecione uma obra!", Toast.LENGTH_SHORT).show();
        } else if(quantidade == null || quantidade == 0L) {
            Toast.makeText(context, "A quantidade é obrigatória e deve ser maior do que 0 (zero)!", Toast.LENGTH_SHORT).show();
        } else if(quantidade > produto.getQuantidadeAtual()) {
            Toast.makeText(context, "A quantidade inserida é maior do que a permitida para retirada desse produto!", Toast.LENGTH_SHORT).show();
        } else {
            String produtoPath = "filiais/" + usuario.getIdFilial() + "/estoque/produtos/" + produto.getId();
            DatabaseReference produtoReference = FirebaseDatabase.getInstance().getReference(produtoPath);

            produto.setQuantidadeAtual(produto.getQuantidadeAtual()-quantidade);

            produtoReference.setValue(produto).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    new AcaoBO(context, usuario, fragmentManager).registrarRetirada(produto, quantidade, obra.getId(), obs);
                }
            });
        }
    }

    public void devolverProduto(Integer quantidade, String obs, Acao retirada) {
        if (quantidade == null || quantidade == 0L) {
            Toast.makeText(context, "A quantidade é obrigatória e deve ser maior do que 0 (zero)!", Toast.LENGTH_SHORT).show();
        } else if(quantidade > retirada.getQuantidadeRetirada() - retirada.getQuantidadeDevolvida()) {
            Toast.makeText(context, "A quantidade inserida é maior do que a permitida para devolução desse produto!", Toast.LENGTH_SHORT).show();
        } else {
            String produtoPath = "filiais/" + usuario.getIdFilial() + "/estoque/produtos/" + retirada.getIdProduto();
            DatabaseReference produtoReference = FirebaseDatabase.getInstance().getReference(produtoPath);

            produtoReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Produto produto = dataSnapshot.getValue(Produto.class);

                        produto.setId(dataSnapshot.getKey());

                        produto.setQuantidadeAtual(produto.getQuantidadeAtual()+quantidade);

                        produtoReference.setValue(produto).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                new AcaoBO(context, usuario, fragmentManager).registrarDevolucao(produto, quantidade, obs, retirada);
                            }
                        });
                    }
                }
            });
        }
    }

    public void editarProduto(Produto produto, String obsAlteracao) {
        produtosReference.child(produto.getId()).setValue(produto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                new AcaoBO(context, usuario, fragmentManager).registrarAlteracaoProduto(produto.getId(), obsAlteracao);
            }
        });
    }

    public void buscarNomeProduto(String idProduto, TextView nomeProdutoView){
        produtosReference.child(idProduto).child("descricao").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String nomeProduto = dataSnapshot.getValue(String.class);

                nomeProdutoView.setText(nomeProduto);
            }
        });
    }

    public void salvarProduto(HashMap<String, Object> params, List<String> fotosPathList, NavigationView navigationView, Button btnSalvar) {
        Long quantidade = (Long) params.get("quantidade");

        if(quantidade == 0L) {
            Toast.makeText(context, "A quantidade deve ser maior do que zero!", Toast.LENGTH_SHORT).show();
        } else {
            String descricao = (String) params.get("descricao");
            String categoria = (String) params.get("categoria");
            String unidadeMedida = (String) params.get("unidadeMedida");
            String data = (String) params.get("data");
            String hora = (String) params.get("hora");
            String obs = params.get("obs") != null ? (String) params.get("obs") : null;

            Produto produto = new Produto();

            produto.setDescricao(descricao);
            produto.setCategoria(categoria);
            produto.setUnidadeMedida(unidadeMedida);
            produto.setQuantidadeInicial(quantidade);
            produto.setQuantidadeAtual(quantidade);
            produto.setData(data);
            produto.setHora(hora);
            produto.setObs(obs);

            produto.setIdUsuario(FirebaseAuth.getInstance().getUid());

            DatabaseReference produtoReference = produtosReference.push();

            produtoReference.setValue(produto)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            produto.setId(produtoReference.getKey());

                            if(fotosPathList.isEmpty()){
                                Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_layout, new NovoProdutoFragment(navigationView, usuario))
                                        .commit();
                            } else {
                                new FotoBO(context, usuario, fragmentManager).salvarFotosProduto(fotosPathList, produto.getId(), navigationView, btnSalvar);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Ocorreu uma falha ao salvar o produto.", Toast.LENGTH_SHORT).show();
                    btnSalvar.setClickable(true);
                }
            });
        }
    }

    public void buscarProdutosRetirarProduto(Spinner spinnerProdutos) {
        produtosReference.get().addOnCompleteListener(runnable ->  {
            try{
                if(runnable.isSuccessful()) {
                    List<Object> listProduto = new ArrayList<>();

                    Produto selecioneProduto = new Produto();

                    selecioneProduto.setDescricao("Selecione o produto");
                    selecioneProduto.setId("selecione");

                    listProduto.add(selecioneProduto);

                    for (DataSnapshot child : runnable.getResult().getChildren()) {
                        Produto produto = child.getValue(Produto.class);
                        produto.setId(child.getKey());
                        listProduto.add(produto);
                    }

                    ArrayAdapter<Object> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, listProduto);
                    spinnerProdutos.setAdapter(adapter);
                } else if(runnable.isCanceled()){
                    Toast.makeText(context, "O carregamento dos produtos foi interrompido.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Ocorreu uma falha no carregamento dos produtos.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Ocorreu uma exceção ao carregar os produtos: " + e.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void buscarProdutosListarProdutos(RecyclerView recyclerViewListarProdutos, NavigationView navigationView) {
        List<Produto> produtos = new ArrayList<>();

        ListarProdutosAdapter produtoAdapter = new ListarProdutosAdapter(context, produtos, usuario, fragmentManager, navigationView);

        recyclerViewListarProdutos.setAdapter(produtoAdapter);

        produtosReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<Produto> list = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Produto produto = child.getValue(Produto.class);

                        if (produto != null) {
                            produto.setId(child.getKey());
                            list.add(produto);
                        }
                    }

                    Collections.reverse(list);

                    for(Produto produto : list) {
                        produtos.add(produto);
                        produtoAdapter.notifyItemInserted(produtos.size() - 1);
                    }
                }
            }
        });
    }

    public void excluirProduto(String idProduto, NavigationView navigationView) {
        produtosReference.child(idProduto).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                new FotoBO(context, usuario, fragmentManager).excluirFotosProduto(idProduto);

                Toast.makeText(context, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new ListarProdutosFragment(usuario, navigationView))
                        .commit();
            }
        });
    }
}
