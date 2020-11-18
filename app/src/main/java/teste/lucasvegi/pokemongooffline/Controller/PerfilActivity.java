package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.Model.PokemonCapturado;
import teste.lucasvegi.pokemongooffline.R;

public class PerfilActivity extends Activity {

    public final static int PERFIL_TROCA = 1;

    public static final int REQUEST_ENABLE_BT = 402;

    private Button troca;

    // Verifica se o usuário habilitou o Bluetooth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_ENABLE_BT) {
                if(resultCode  == RESULT_OK) {
                    Intent it = new Intent(this, TrocaListaUsuariosActivity.class);
                    startActivityForResult(it,PERFIL_TROCA);

                } else if (resultCode == RESULT_CANCELED){

                    Context context = getApplicationContext();
                    CharSequence text = "Seu Bluetooth está desligado. Ative-o para realizar troca de pokémons.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        } catch (Exception e) {
            Log.e("PERFIL", "ERRO: " + e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //obtem referências das views
        ImageView imageView = (ImageView) findViewById(R.id.imgTreinadorPerfil);
        TextView txtInicioAventura = (TextView) findViewById(R.id.txtInicioAventuraPerfil);
        TextView txtNumCapturas = (TextView) findViewById(R.id.txtNumCapturasPerfil);
        TextView txtNomeTreinador = (TextView) findViewById(R.id.txtNomeTreinadorPerfil);
        troca = (Button) findViewById(R.id.buttonTroca);

        try {
            //Define o nome do treinador
            txtNomeTreinador.setText(ControladoraFachadaSingleton.getInstance().getUsuario().getLogin());

            //Define a imagem do perfil baseando-se no sexo do usuário
            if(ControladoraFachadaSingleton.getInstance().getUsuario().getSexo().equals("M"))
                imageView.setImageResource(R.drawable.male_grande);
            else
                imageView.setImageResource(R.drawable.female_grande);

            //Define o início da aventura
            txtInicioAventura.setText(ControladoraFachadaSingleton.getInstance().getUsuario().getDtCadastro());

            //Define o número de pokemons capturados pelo usuário
            int contCaptura = 0;
            for (Map.Entry<Pokemon,List<PokemonCapturado>> entry : ControladoraFachadaSingleton.getInstance().getUsuario().getPokemons().entrySet()){
                contCaptura += entry.getValue().size();
            }
            txtNumCapturas.setText(contCaptura+"");

        }catch (Exception e){
            Log.e("PERFIL", "ERRO: " + e.getMessage());
        }
    }

    public void clickTroca(View v) {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Context context = getApplicationContext();
                CharSequence text = "Seu dispositivo não suporta Bluetooth: a troca de pokémons esta'desabilitada para você.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                troca.setEnabled(false);

                return;
            }
            else if (!bluetoothAdapter.isEnabled()) {

                if(!troca.isEnabled())
                    troca.setEnabled(true);

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                onActivityResult(REQUEST_ENABLE_BT, 8989, enableBtIntent);
            }
            else  {
                if(!troca.isEnabled())
                    troca.setEnabled(true);

                Intent it = new Intent(this, TrocaListaUsuariosActivity.class);
                startActivityForResult(it,PERFIL_TROCA);
            }

        } catch (Exception e){
            Log.e("TROCA", "ERRO: " + e.getMessage());
        }
    }

    public void clickLogout(View v){
        try {
            Log.i("LOGOUT", "Saindo...");

            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("SAIR");
            alerta.setMessage("Deseja finalizar essa sessão?");

            //Configura ação para confirmação positiva
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ControladoraFachadaSingleton.getInstance().logoutUser();
                    setResult(MapActivity.MENU_PERFIL);

                    finish();
                }
            });

            //Configura ação para negação da ação
            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            //Exibe janela de confirmação
            alerta.show();

        }catch (Exception e){
            Log.e("LOGOUT", "ERRO: " + e.getMessage());
        }

    }

    public void clickVoltar(View v){
        finish();
    }
}
