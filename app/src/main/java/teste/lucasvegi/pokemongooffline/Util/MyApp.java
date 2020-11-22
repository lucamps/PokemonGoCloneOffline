package teste.lucasvegi.pokemongooffline.Util;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Lucas on 12/12/2016.
 */
public class MyApp extends Application {

    private static Context context;
    private static BluetoothSocket bluetoothSocket;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        //método usado para recuperar o context do app
        //de qualquer parte do programa
        return MyApp.context;
    }

    public static BluetoothSocket getBluetoothSocket() {
        return MyApp.bluetoothSocket;
    }

    public static void setBluetoothSocket(BluetoothSocket socket){
        MyApp.bluetoothSocket = socket;
    }

    public static void setPokemon(int id) {

        // Criando stream de dados para o socket(host, port) em @param bluetoothSocket
        try {
            ObjectOutputStream out = new ObjectOutputStream(bluetoothSocket.getOutputStream());
            out.writeInt(id);
        } catch (Exception e) {
            Log.e("SOCKET", "Falha ao enviar o pokémon");
        }

    }
    public static int getPokemon() {

        // Recebendo o stream de dados
        try {
            ObjectInputStream out = new ObjectInputStream(bluetoothSocket.getInputStream());
            int id = out.readInt();
            return id;
        } catch (Exception e) {
            Log.e("SOCKET", "Falha ao receber o pokémon");
        }

        return -1;
    }

}
