package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import teste.lucasvegi.pokemongooffline.R;

import static teste.lucasvegi.pokemongooffline.Controller.PerfilActivity.PERFIL_TROCA;

public class TrocaListaUsuariosActivity extends Activity{

    static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<String> avaliableUsers = new ArrayList<String>();
    List<BluetoothDevice> bluetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_usuarios);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

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

        //zera a lista
        bluetoothDevices = null;

        //inicializa a lista
        bluetoothDevices = new ArrayList<BluetoothDevice>();

        //busca dispositivos
        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                
                //Adiciona o dispositivo na lista
                bluetoothDevices.add(device);

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                if(deviceName != null)
                    avaliableUsers.add(deviceName + "," + deviceHardwareAddress);

            }
        }
    };

    public void updateBT(View v) {

        avaliableUsers.clear();
        bluetoothDevices.clear();

        bluetoothAdapter.startDiscovery();
    }

    public void listar(View v) {

        TextView t = (TextView) findViewById(R.id.lista_usuarios);
        String s = "";
        for(int i=0; i<avaliableUsers.size(); i++) {
            s += avaliableUsers.get(i) + "\n";
        }
        t.setText(s);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int idx, long id) {
        // Recupera o device selecionado
        BluetoothDevice device = bluetoothDevices.get(idx);
        String msg = device.getName() + " - " + device.getAddress();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // Abre a tela do chat
        Intent intent = new Intent(this, TrocaListaPokemonActivity.class);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        startActivity(intent);

    }
}
