package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.MyApp;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;
import teste.lucasvegi.pokemongooffline.View.AdapterTrocaPokemonsList;

public class TrocaListaPokemonActivity extends Activity implements AdapterView.OnItemClickListener{

    private List<Pokemon> pokemons;

    static final int REQUEST_ENABLE_BT = 1;

    protected static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice device;
    private BluetoothSocket socket;

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

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        socket = MyApp.getBluetoothSocket();

        if(socket != null)
            Log.e("TROCA", "Socket encontrado");
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
