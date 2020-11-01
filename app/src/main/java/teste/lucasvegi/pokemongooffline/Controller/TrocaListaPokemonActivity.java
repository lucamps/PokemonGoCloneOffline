package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;
import teste.lucasvegi.pokemongooffline.View.AdapterTrocaPokemonsList;

public class TrocaListaPokemonActivity extends Activity implements AdapterView.OnItemClickListener{

    private List<Pokemon> pokemons;

    boolean pode_alterar_oferta = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_pokemons);

        //obtem referências das views

        try {
            //Prepara a listview customizada da pokedex
            pokemons = ControladoraFachadaSingleton.getInstance().getPokemons();
            ListView listView = (ListView) findViewById(R.id.listaTrocaPokemons);

            AdapterTrocaPokemonsList adapterPokedex = new AdapterTrocaPokemonsList(pokemons, this);
            listView.setAdapter(adapterPokedex);
            listView.setOnItemClickListener(this);


        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if(!pode_alterar_oferta) {
                Context context = getApplicationContext();
                CharSequence text = "Você não pode trocar o Pokémon que está oferecendo! Aperte Rejeitar para trocar sua oferta.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                return;
            }

            //Click em um item da listView customizada
            Pokemon pkmn = (Pokemon) parent.getAdapter().getItem(position);

            ImageView pokemon_selecionado = (ImageView) findViewById(R.id.meu_pokemon_selecionado);
            pokemon_selecionado.setImageResource(pkmn.getIcone());

        }catch (Exception e){
            Log.e("POKEDEX", "ERRO no click: " + e.getMessage());
        }

    }

    public void alterarOfertaTroca(View v){    }

    public void aceitarTroca(View v){
        pode_alterar_oferta = false;
    }

    public void rejeitarTroca(View v){
        pode_alterar_oferta = true;
    }

    public void clickVoltar(View v){
        finish();
    }
}
