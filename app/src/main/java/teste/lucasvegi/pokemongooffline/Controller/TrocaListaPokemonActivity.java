package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import teste.lucasvegi.pokemongooffline.R;

public class TrocaListaPokemonActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_pokemons);

        //obtem referências das views

        try {


        }catch (Exception e){
            Log.e("TROCA", "ERRO: " + e.getMessage());
        }
    }

    public void clickVoltar(View v){
        finish();
    }
}
