package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import teste.lucasvegi.pokemongooffline.R;

public class TrocaPropostaActivity extends Activity{

    Button alterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_proposta);

        try {
            alterar = (Button) findViewById(R.id.botaoAlterar);

        }catch (Exception e){
            Log.e("TROCA", "ERRO: " + e.getMessage());
        }
    }

    public void alterarOfertaTroca(View v){

    }

    public void aceitarTroca(View v){
        alterar.setEnabled(false);
    }

    public void rejeitarTroca(View v){
        alterar.setEnabled(true);
    }

    public void clickVoltar(View v){
        finish();
    }
}
