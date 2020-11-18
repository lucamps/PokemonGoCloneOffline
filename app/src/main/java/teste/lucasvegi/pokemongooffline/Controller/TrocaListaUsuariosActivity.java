package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import teste.lucasvegi.pokemongooffline.R;

import static teste.lucasvegi.pokemongooffline.Controller.PerfilActivity.PERFIL_TROCA;

public class TrocaListaUsuariosActivity extends Activity{

    static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_usuarios);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CharSequence text;
        if (bluetoothAdapter == null) {
            text = "Deu ruim";

        } else {
            text = "Deu bom";
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

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
