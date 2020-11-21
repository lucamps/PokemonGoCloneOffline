package teste.lucasvegi.pokemongooffline.View;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import teste.lucasvegi.pokemongooffline.R;

public class AdapterTroca extends BaseAdapter {

    private List<BluetoothDevice> devices;
    private Activity activity;

    public AdapterTroca(List<BluetoothDevice> _devices, Activity _activity) {
        try {
            this.devices = _devices;
            this.activity = _activity;
        }catch (Exception e){
            Log.e("LISTA_USUARIOS", "ERRO: " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {

            View view = activity.getLayoutInflater().inflate(R.layout.lista_troca_personalizada, parent, false);
            BluetoothDevice dev = devices.get(position);

            Log.i("LISTA_USUARIOS", "Montando lista para " + dev.getName());
            TextView device_name = (TextView)
                    view.findViewById(R.id.troca_deviceName);
            device_name.setText(dev.getName());

            return view;

        }catch (Exception e){
            Log.e("LISTA_USUARIOS", "ERRO: " + e.getMessage());
            return null;
        }
    }

}
