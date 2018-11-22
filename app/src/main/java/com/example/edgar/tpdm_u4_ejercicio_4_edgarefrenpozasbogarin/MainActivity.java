package com.example.edgar.tpdm_u4_ejercicio_4_edgarefrenpozasbogarin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txt_caja_1,txt_caja_2,txt_caja_3;
    private ListView ls_caja_1,ls_caja_2,ls_caja_3;
    private ArrayList<Caja> cajas;
    private boolean iniciado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_caja_1=findViewById(R.id.caja_1);
        txt_caja_2=findViewById(R.id.caja_2);
        txt_caja_3=findViewById(R.id.caja_3);

        ls_caja_1=findViewById(R.id.ls_caja1);
        ls_caja_2=findViewById(R.id.ls_caja2);
        ls_caja_3=findViewById(R.id.ls_caja3);


    }

    public void iniciar(View view) {
        iniciado=true;
        cajas=new ArrayList<>();
        Caja caja=new Caja(0,txt_caja_1,ls_caja_1);
        cajas.add(caja);
        Caja caja2=new Caja(1,txt_caja_2,ls_caja_2);
        cajas.add(caja2);
        Caja caja3=new Caja(2,txt_caja_3,ls_caja_3);
        cajas.add(caja3);

        for (Caja c:cajas) {
            c.iniciar();
        }
    }

    class Cliente{
        public  int numero_tramites;

        public Cliente(int numero_tramites) {
            this.numero_tramites = numero_tramites;
        }
    }
    class Caja{
        private boolean ocupado;
        private int numero_atendidos;
        private int id;
        private TextView txt_caja;
        private ListView fila;
        public ArrayList<Cliente> clientes;

        public Caja(int id, TextView txt_caja, ListView caja) {
            this.id = id;
            this.txt_caja = txt_caja;
            this.fila = caja;
            clientes=new ArrayList<>();
        }
        public void iniciar(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (iniciado){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Caja aux=cajas.get(0);
                                for(Caja c:cajas){
                                    for(Caja c2:cajas){
                                        if(c.clientes.size()<c2.clientes.size()){
                                            aux=c;
                                        }
                                    }
                                }
                                if(aux!=null){
                                    if(aux==Caja.this) {
                                        Cliente c = new Cliente((int) (Math.random() * 7) + 1);
                                        clientes.add(c);
                                    }
                                }

                                actualizar_lista();

                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(iniciado){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(numero_atendidos>=20){
                                    txt_caja.setText("Espera");
                                    ocupado=true;
                                }
                            }
                        });

                        if(ocupado) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            numero_atendidos=0;
                            ocupado=false;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_caja.setText("Caja "+(id+1)+"\n"+numero_atendidos+"/20");
                            }
                        });

                        if(clientes.size()==0)
                            continue;

                        Cliente c=clientes.get(0);
                        c.numero_tramites--;

                        if(c.numero_tramites<=0){
                            clientes.remove(0);
                            numero_atendidos++;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                actualizar_lista();
                            }
                        });


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        private void actualizar_lista(){
            ArrayList<String> datos=new ArrayList<>();
            for (Cliente c:clientes){
                datos.add(c.numero_tramites+"");
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,datos);
            fila.setAdapter(adapter);
        }
    }
}
