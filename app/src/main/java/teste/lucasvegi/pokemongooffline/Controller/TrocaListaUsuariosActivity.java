package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import teste.lucasvegi.pokemongooffline.R;

import static teste.lucasvegi.pokemongooffline.Controller.PerfilActivity.PERFIL_TROCA;

public class TrocaListaUsuariosActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_usuarios);

        //obtem referências das views

        try {


        }catch (Exception e){
            Log.e("TROCA", "ERRO: " + e.getMessage());
        }
    }

    public void exchange(View v) {
        try {
//            Intent it = new Intent(this, TrocaListaPokemonActivity.class);
//            startActivityForResult(it,PERFIL_TROCA);
            Intent it = new Intent(this, TrocaListaPokemonActivity.class);
            startActivityForResult(it,PERFIL_TROCA);
        } catch (Exception e){
            Log.e("TROCA", "ERRO: " + e.getMessage());
        }
    }

    public void clickVoltar(View v){
        finish();
    }
}
