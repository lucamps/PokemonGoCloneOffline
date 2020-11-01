package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;

public class TrocaListaPokemonActivity extends Activity implements AdapterView.OnItemClickListener{

    private List<Pokemon> pokemons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_pokemons);

        //obtem referências das views

        try {

            //Prepara a listview customizada da pokedex
            pokemons = ControladoraFachadaSingleton.getInstance().getPokemons();
            ListView listView = (ListView) findViewById(R.id.listaTrocaPokemons);

            AdapterPokedex adapterPokedex = new AdapterPokedex(pokemons, this);
            listView.setAdapter(adapterPokedex);
            listView.setOnItemClickListener(this);


        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            //Click em um item da listView customizada
            Pokemon pkmn = (Pokemon) parent.getAdapter().getItem(position);

            //verifica se pokemon selecionado já foi capturado pelo menos  uma vez
            if (ControladoraFachadaSingleton.getInstance().getUsuario().getQuantidadeCapturas(pkmn) > 0) {

                //Toast.makeText(this, "Detalhes do " + pkmn.getNome(), Toast.LENGTH_SHORT).show();

                Intent it = new Intent(this,TrocaPropostaActivity.class);
                it.putExtra("pkmn",pkmn);
                startActivity(it);
            }

        }catch (Exception e){
            Log.e("POKEDEX", "ERRO no click: " + e.getMessage());
        }

    }

    public void clickVoltar(View v){
        finish();
    }
}
