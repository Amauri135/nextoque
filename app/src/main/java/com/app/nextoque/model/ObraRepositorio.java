package com.app.nextoque.model;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nextoque.entity.Obra;
import com.app.nextoque.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ObraRepositorio {
    private Context context;
    private DatabaseReference obraReference;
    private String idFilialUsuario;

    public ObraRepositorio(Context context,String idFilialUsuario) {
        this.context = context;
        this.idFilialUsuario = idFilialUsuario;
        this.obraReference = FirebaseDatabase.getInstance().getReference("filiais/" + idFilialUsuario + "/obras");
    }
    
    public void buscarObras(Spinner spinnerObras) {
        obraReference.get().addOnCompleteListener(runnable ->  {
            try{
                if(runnable.isSuccessful()) {
                    List<Object> listObra = new ArrayList<>();

                    listObra.add("");

                    for (DataSnapshot child : runnable.getResult().getChildren()) {
                        Obra obra = child.getValue(Obra.class);
                        obra.setId(child.getKey());
                        listObra.add(obra);
                    }

                    ArrayAdapter<Object> adapter = new ArrayAdapter<>(context, R.layout.list_item_spinner, listObra);
                    spinnerObras.setAdapter(adapter);
                } else if(runnable.isCanceled()){
                    Toast.makeText(context, "O carregamento das obras foi interrompido.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Ocorreu uma falha no carregamento das obras.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Ocorreu uma exceção ao carregar as obras: " + e.getCause().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void buscarNomeObra(String idObra, TextView obra) {
        obraReference.child(idObra).child("nome_obra").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String nomeObra = dataSnapshot.getValue(String.class);

                obra.setText(nomeObra);
            }
        });
    }
}
