package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;
import teste.lucasvegi.pokemongooffline.View.AdapterTroca;

import static teste.lucasvegi.pokemongooffline.Controller.PerfilActivity.PERFIL_TROCA;

public class TrocaListaUsuariosActivity extends Activity implements AdapterView.OnItemClickListener{

    static final int REQUEST_ENABLE_BT = 1;
    protected static final String NAME = "SERVIDOR_TROCAS";
    protected static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    List<BluetoothDevice> bluetoothDevices = null;

    AcceptThread acceptThread;

    // Complemento do comportamento do botão @updateBT
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Button btn = (Button) findViewById(R.id.buscar);

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // No início da busca, alerta o usuário para o período de espera
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, "Aguarde...", duration);
                toast.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Habilita o botão novamente e restaura a cor original
                btn.setEnabled(true);
                btn.setTextColor(Color.parseColor("#000000"));
                listar();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_lista_usuarios);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

         if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Inicializa a lista
        bluetoothDevices = new ArrayList<BluetoothDevice>();

        //inicia o servidor
        acceptThread = new AcceptThread();
        acceptThread.start();

        //busca dispositivos
        //bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //Adiciona o dispositivo na lista
                if(device.getName() != null)
                bluetoothDevices.add(device);

            }
        }
    };

    public void updateBT(View v) {

        Button btn = (Button) findViewById(R.id.buscar);
        btn.setTextColor(Color.parseColor("#808080"));
        btn.setEnabled(false);

        bluetoothDevices.clear();

        // Filtro de controle para o método assíncrono startDiscovery
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    public void listar() {

        try {

            //Prepara a ListView customizada da troca de usuários
            ListView listView = (ListView) findViewById(R.id.bluetooth_user_list);
            AdapterTroca adapterTroca = new AdapterTroca(bluetoothDevices, this);

            listView.setAdapter(adapterTroca);
            listView.setOnItemClickListener(this);

        }catch (Exception e){
            Log.e("TROCA", "ERRO AO LISTAR: " + e.getMessage());
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
        acceptThread.cancel();
        unregisterReceiver(mReceiver);
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

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
            } catch (IOException e) {
                Log.e("Socket Fail", "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.

            Log.e("AcceptThread", "Running");

            while (true) {
                try {
                    socket = mmServerSocket.accept();

                    if (socket != null) {
                        // A connection was accepted. Perform work associated with
                        // the connection in a separate thread.
                        manageMyConnectedSocket(socket);
                        mmServerSocket.close();
                        break;
                    }
                } catch (IOException e) {
                    Log.e("Socket Fail", "Socket's accept() method failed", e);
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {

            Log.e("AcceptThread", "Closing");

            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("Socket Fail", "Could not close the connect socket", e);
            }
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket){
        Toast.makeText(this, "Tentaram conectar", Toast.LENGTH_LONG).show();
        acceptThread.cancel();
    }
}
