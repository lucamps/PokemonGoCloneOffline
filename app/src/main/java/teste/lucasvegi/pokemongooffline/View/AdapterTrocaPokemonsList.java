package teste.lucasvegi.pokemongooffline.View;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;

/**
 * Created by Lucas on 20/12/2016.
 */
public class AdapterTrocaPokemonsList extends BaseAdapter {

    private List<Pokemon> pokemons;
    private Activity act;
    private boolean areAllEnabled = true;
    private int selected = -1;

    public AdapterTrocaPokemonsList(List<Pokemon> pokemons, Activity act) {
        try {
            this.pokemons = pokemons;
            this.act = act;


            //carregarBitmapsNoCache();
        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return pokemons.size();
    }

    @Override
    public Object getItem(int position) {
        return pokemons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pokemons.get(position).getNumero();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            View view = act.getLayoutInflater().inflate(R.layout.lista_pokedex_personalizada_trocas, parent, false);
            View view2 = act.getLayoutInflater().inflate(R.layout.lista_pokedex_personalizada_null, parent, false);
            Pokemon pkmn = pokemons.get(position);

            Log.i("POKEDEX", "Montando lista pokedex para " + pkmn.getNome());

            TextView nomePokemon = (TextView)
                    view.findViewById(R.id.txtNomePokemonPokedex);
            TextView numeroPossuidos = (TextView)
                    view.findViewById(R.id.txtNumeroPossuidosPokedex);
            TextView numeroPokemon = (TextView)
                    view.findViewById(R.id.txtNumeroPokemonPokedex);
            ImageView imagem = (ImageView)
                    view.findViewById(R.id.imagemPokemonPokedex);

            int numPossuidos = ControladoraFachadaSingleton.getInstance().getUsuario().getQuantidadeCapturas(pkmn,false);
            //Decide se vai ter informações do pokemon ou não
            if(numPossuidos > 0) {
                nomePokemon.setText(pkmn.getNome());
                numeroPossuidos.setText("Qte: " + String.valueOf(numPossuidos));

                //ajusta o visual do número acrescendo zeros ao lado
                if(pkmn.getNumero() < 10)
                    numeroPokemon.setText("#00"+pkmn.getNumero());
                else if(pkmn.getNumero() < 100)
                    numeroPokemon.setText("#0"+pkmn.getNumero());
                else
                    numeroPokemon.setText("#" + pkmn.getNumero());

                imagem.setImageResource(pkmn.getIcone());
            }else {
                return view2;
            }

            return view;
        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return areAllEnabled;
    }

    @Override
    public boolean isEnabled(int position) {
        return (position != selected) && areAllItemsEnabled();
    }

    public void setAreAllEnabled(boolean areAllEnabled) {
        this.areAllEnabled = areAllEnabled;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
